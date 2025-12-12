package com.khao.PoorDeal.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.domain.ProcessStatus;
import com.khao.PoorDeal.domain.ProcessType;
import com.khao.PoorDeal.dto.ActionType;
import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostResponse;
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
                () -> new NoSuchElementException("해당 쪽지가 존재하지 않습니다: " + mailId));
    }
    
    public boolean hasApplied(Long senderId, Long postId) {
        return mailRepository.existsBySenderIdAndPostId(senderId, postId);
    }
    
    @Transactional
    public void startNewProcess(StartProcessRequest request, Long senderId) {
        
        Member sender = memberRepository.findById(senderId).orElseThrow(
                () -> new UsernameNotFoundException(senderId + " 사용자를 찾을 수 없습니다."));
        
        if (request.getPostId() == null || request.getReceiverId() == null) {
            throw new IllegalArgumentException("필수 요청 정보가 누락되었습니다.");
        }

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
    
    @Transactional
    public void handleProcessAction(ProcessActionRequest request, Long senderId) {
        
        Member sender = memberRepository.findById(senderId).orElseThrow(
                () -> new UsernameNotFoundException(senderId + " 사용자를 찾을 수 없습니다."));

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
                // 2-1. 구매자가 판매자에게 조건 최종 수락 (거래 중 상태로 변경)
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
                            .price(post.getPrice())
                            .hiringQuota(post.getHiringQuota())
                            .filledCount(post.getFilledCount())
                            .build());
                    
                    // 송금 요청 메일 자동 발송
                    nextStatus = ProcessStatus.TRADE_TRANSFER_PENDING;
                    newTitle = receiver.getUserName() + "님의 송금 요청";
                    
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
                // 2-2. 구매자가 판매자에게 조건 최종 거절 (거래 취소)
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
                            .price(post.getPrice())
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
                    
                    if (!result) {
                        // 송금 실패
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
                                .content("잔액 부족 등의 사유로 송금에 실패했습니다.")
                                .build();
                            
                        mailRepository.save(newMail1);
                    } else {
                        // 송금 성공 -> 거래 완료
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
                }
                break;

            case RECRUIT:
                // 1-1. 구인 요청 수락 (채용)
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
                            .price(post.getPrice())
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
                // 1-2. 구인 요청 거절
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
                            .price(post.getPrice())
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