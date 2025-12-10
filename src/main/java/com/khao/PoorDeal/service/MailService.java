package com.khao.PoorDeal.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.domain.ProcessStatus;
import com.khao.PoorDeal.domain.ProcessType;
import com.khao.PoorDeal.dto.ActionType;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.ProcessActionRequest;
import com.khao.PoorDeal.dto.StartProcessRequest;
import com.khao.PoorDeal.repository.MailRepository;
import com.khao.PoorDeal.repository.MemberRepository;
import com.khao.PoorDeal.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	private final TradeService tradeService;
	private final MailRepository mailRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	
	public int getMailCountByMemberId(Long memberId) {
		
		return mailRepository.selectMailCountByMemberId(memberId);
	}
	
	public List<MailSummary> getPagedMailsSummaryByMemberId(int offset, int pagesize, Long memberId) {
		
		return mailRepository.selectPagedMailsByMemberId(offset, pagesize, memberId);
	}
	
	public MailResponse getMailResponse(Long mailId) {
		
		return mailRepository.findMailResponseById(mailId).orElseThrow(
				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다: " + mailId));
	}
	
	public boolean hasApplied(Long senderId, Long postId) {
		
		return mailRepository.existsBySenderIdAndPostId(senderId, postId);
	}
	
	public void startNewProcess(StartProcessRequest request, Long senderId) {
		
		Member sender = memberRepository.findById(senderId).orElseThrow(
				() -> new UsernameNotFoundException(senderId + " 사용자를 찾을 수 없습니다."));
		
		// 1. 유효성 검사 (게시글 및 수신자 존재 여부 등)
        if (request.getPostId() == null || request.getReceiverId() == null) {
            throw new IllegalArgumentException("필수 요청 정보가 누락되었습니다.");
        }

        // 2. 프로세스 타입에 따른 초기 상태 설정
        ProcessStatus initialStatus = null;
        String title = null;
        if (request.getProcessType() == ProcessType.TRADE) {
            initialStatus = ProcessStatus.TRADE_REQUEST_PENDING;
            title = sender.getUserName() + "님이 거래를 요청하였습니다.";
        } else if (request.getProcessType() == ProcessType.RECRUIT) {
            initialStatus = ProcessStatus.RECRUIT_REQUEST_PENDING;
            title = sender.getUserName() + "님이 구직을 요청하였습니다.";

            PostResponse post = postRepository.findById(request.getPostId()).orElseThrow(
    				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다"));
            postRepository.update(Post.builder()
            		.id(post.getId())
            		.title(post.getTitle())
            		.content(post.getContent())
            		.authorId(post.getAuthorId())
            		.image(post.getImage())
            		.type(post.getType())
            		.isAvailable(false)
            		.price(post.getPrice())
            		.hiringQuota(post.getHiringQuota())
            		.filledCount(post.getFilledCount())
            		.build());
        } else {
             throw new IllegalArgumentException("유효하지 않은 프로세스 타입입니다.");
        }
        
        Mail newMail = Mail.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .postId(request.getPostId())
                .processType(request.getProcessType())
                .processStatus(initialStatus)
                .title(title)
                .content(request.getContent())
                .amount(request.getAmount())
                .build();
        
        mailRepository.save(newMail);
	}
	
	public void handleProcessAction(ProcessActionRequest request, Long senderId) {
		
		// 0. 사용자 조회
        Member sender = memberRepository.findById(senderId).orElseThrow(
				() -> new UsernameNotFoundException(senderId + " 사용자를 찾을 수 없습니다."));

        // 1. previousMail 조회
        Mail previousMail = mailRepository.findById(request.getMailId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쪽지 ID입니다."));
        
        Member receiver = memberRepository.findById(previousMail.getSenderId()).orElseThrow(
				() -> new UsernameNotFoundException(previousMail.getSenderId() + " 사용자를 찾을 수 없습니다."));
        
        ProcessType previousType = previousMail.getProcessType();
        ProcessStatus previousStatus = previousMail.getProcessStatus();
        ActionType action = request.getActionType();
        
        ProcessStatus nextStatus = null;
        String newTitle = null;
        String newContent = request.getContent();
        
        // 이전 메일의 수신자 and 보낼 메일의 발신자 and 현재 주체
        // Long senderId;
        // 이전 메일의 발신자 and 보낼 메일의 수신자
        Long receiverId = Objects.equals(previousMail.getReceiverId(), senderId) ?
                                previousMail.getSenderId() : previousMail.getReceiverId();
        
        switch (previousType) {
            case TRADE:
            	// 1. 판매자가 구매자에게 조건 제시
                if (previousStatus == ProcessStatus.TRADE_REQUEST_PENDING && 
                	action == ActionType.SET_CONDITIONS) {
                    
                    nextStatus = ProcessStatus.TRADE_CONDITIONS_SET;
                    newTitle = sender.getUserName() + "님이 거래 조건을 제시했습니다.";
                    newContent = (newContent != null ? newContent + "\n\n" : "") +
                                String.format("거래 조건:\n시간: %s\n장소: %s", request.getTradeTime(), request.getTradeLocation());
                    
                    Mail newMail = Mail.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail);
                    
                    previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                    mailRepository.updateMailStatus(previousMail);
                }
                // 2-1. 구매자가 판매자에게 조건 최종 수락
                else if (previousStatus == ProcessStatus.TRADE_CONDITIONS_SET && 
                		action == ActionType.ACCEPT_TRADE) {
                    
                    nextStatus = ProcessStatus.TRADE_ACCEPTED;
                    newTitle = sender.getUserName() + "님이 거래를 수락했습니다.";

                    Mail newMail1 = Mail.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail1);
                    
                    PostResponse post = postRepository.findById(request.getPostId()).orElseThrow(
            				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다"));
                    postRepository.update(Post.builder()
                    		.id(post.getId())
                    		.title(post.getTitle())
                    		.content(post.getContent())
                    		.authorId(post.getAuthorId())
                    		.image(post.getImage())
                    		.type(post.getType())
                    		.isAvailable(false)
                    		.hiringQuota(post.getHiringQuota())
                    		.filledCount(post.getFilledCount())
                    		.build());
                    
                    // 송금 메일
                    
                    nextStatus = ProcessStatus.TRADE_TRANSFER_PENDING;
                    newTitle = receiver.getUserName() + "님의 송금 요청";
                    //newContent = receiver.getUserName() + "님에게 "
                    //		+ previousMail.getAmount() + "포인트 송금하기";
                    
                    Mail newMail2 = Mail.builder()
                            .senderId(receiverId)
                            .receiverId(senderId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail2);
                    
                    previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                    mailRepository.updateMailStatus(previousMail);
                }
                // 2-2. 구매자가 판매자에게 조건 최종 거절
                else if (previousStatus == ProcessStatus.TRADE_CONDITIONS_SET && 
                		action == ActionType.REJECT_TRADE) {
                    
                    nextStatus = ProcessStatus.TRADE_REJECTED;
                    newTitle = sender.getUserName() + "님이 거래를 거절했습니다.";

                    PostResponse post = postRepository.findById(request.getPostId()).orElseThrow(
            				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다"));
                    postRepository.update(Post.builder()
                    		.id(post.getId())
                    		.title(post.getTitle())
                    		.content(post.getContent())
                    		.authorId(post.getAuthorId())
                    		.image(post.getImage())
                    		.type(post.getType())
                    		.isAvailable(true)
                    		.hiringQuota(post.getHiringQuota())
                    		.filledCount(post.getFilledCount())
                    		.build());
                    
                    Mail newMail = Mail.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail);
                    
                    previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                    mailRepository.updateMailStatus(previousMail);
                }
                // 3. 구매자가 판매자에게 송금 하기
                else if (previousStatus == ProcessStatus.TRADE_TRANSFER_PENDING &&
                		action == ActionType.TRANSFER) {
                    
                	boolean result = tradeService.tradePoint(senderId, receiverId, previousMail.getAmount());
                	
                	// 송금 실패
                	if (!result) {
                		
                		nextStatus = ProcessStatus.TRADE_TRANSFER_PENDING;
                        newTitle = "송금에 실패하였습니다.";
                        
                        Mail newMail1 = Mail.builder()
                                .senderId(receiverId)
                                .receiverId(senderId)
                                .postId(previousMail.getPostId())
                                .processType(previousMail.getProcessType())
                                .amount(previousMail.getAmount())
                                .processStatus(nextStatus)
                                .title(newTitle)
                                .content(newContent)
                                .build();
                            
                        mailRepository.save(newMail1);
                        
                        // 송금 메일
                        
                        nextStatus = ProcessStatus.TRADE_TRANSFER_PENDING;
                        newTitle = receiver.getUserName() + "님의 송금 요청";
                        //newContent = receiver.getUserName() + "님에게 "
                        //		+ previousMail.getAmount() + "포인트 송금하기";
                        
                        Mail newMail2 = Mail.builder()
                                .senderId(receiverId)
                                .receiverId(senderId)
                                .postId(previousMail.getPostId())
                                .processType(previousMail.getProcessType())
                                .amount(previousMail.getAmount())
                                .processStatus(nextStatus)
                                .title(newTitle)
                                .content(newContent)
                                .build();
                            
                        mailRepository.save(newMail2);
                        
                        previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                        mailRepository.updateMailStatus(previousMail);
                        
                        break;
                	}
                	
                    nextStatus = ProcessStatus.TRADE_PROCESS_COMPLETED;
                    newTitle = sender.getUserName() + "님이 송금을 완료했습니다.";
                    
                    Mail newMail1 = Mail.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail1);
                    
                    newTitle = "송금을 완료했습니다.";
                    
                    Mail newMail2 = Mail.builder()
                            .senderId(receiverId)
                            .receiverId(senderId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail2);
                    
                    previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                    mailRepository.updateMailStatus(previousMail);
                }
                break;

            case RECRUIT:
            	// 1-1. 구인 요청 수락
                if (previousStatus == ProcessStatus.RECRUIT_REQUEST_PENDING && 
                	action == ActionType.ACCEPT_RECRUIT) {
                    
                    nextStatus = ProcessStatus.RECRUIT_ACCEPTED;
                    newTitle = sender.getUserName() + "님이 당신을 채용했습니다.";

                    PostResponse post = postRepository.findById(request.getPostId()).orElseThrow(
            				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다"));
                    postRepository.update(Post.builder()
                    		.id(post.getId())
                    		.title(post.getTitle())
                    		.content(post.getContent())
                    		.authorId(post.getAuthorId())
                    		.image(post.getImage())
                    		.type(post.getType())
                    		.isAvailable(post.isAvailable())
                    		.hiringQuota(post.getHiringQuota())
                    		.filledCount(post.getFilledCount() + 1)
                    		.build());
                    
                    Mail newMail = Mail.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail);
                    
                    previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                    mailRepository.updateMailStatus(previousMail);
                }
                // 1-1. 구인 요청 거절
                else if (previousStatus == ProcessStatus.RECRUIT_REQUEST_PENDING && 
                    	action == ActionType.REJECT_RECRUIT) {
                	
                    nextStatus = ProcessStatus.RECRUIT_REJECTED;
                    newTitle = sender.getUserName() + "님이 당신을 채용하지 않았습니다.";

                    PostResponse post = postRepository.findById(request.getPostId()).orElseThrow(
            				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다"));
                    postRepository.update(Post.builder()
                    		.id(post.getId())
                    		.title(post.getTitle())
                    		.content(post.getContent())
                    		.authorId(post.getAuthorId())
                    		.image(post.getImage())
                    		.type(post.getType())
                    		.isAvailable(false)
                    		.hiringQuota(post.getHiringQuota())
                    		.filledCount(post.getFilledCount())
                    		.build());
                    
                    Mail newMail = Mail.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .postId(previousMail.getPostId())
                            .processType(previousMail.getProcessType())
                            .amount(previousMail.getAmount())
                            .processStatus(nextStatus)
                            .title(newTitle)
                            .content(newContent)
                            .build();
                        
                    mailRepository.save(newMail);
                    
                    previousMail.setProcessStatus(ProcessStatus.PROCESSED);
                    mailRepository.updateMailStatus(previousMail);
                }
                break;
            
            default:
                throw new IllegalStateException("잘못된 프로세스 타입입니다.");
        }
	}
}
