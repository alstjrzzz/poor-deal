package com.khao.PoorDeal.controller;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.dto.CommentAndRepliesResponse;
import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.dto.ProcessActionRequest;
import com.khao.PoorDeal.dto.StartProcessRequest;
import com.khao.PoorDeal.repository.PostRepository;
import com.khao.PoorDeal.service.MailService;
import com.khao.PoorDeal.service.MemberService;
import com.khao.PoorDeal.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailController {

	private int PAGESIZE = 10;
	private int BLOCK_SIZE = 10;
	
	private final MailService mailService;
	private final MemberService memberService;
	private final PostRepository postRepository;
	
	/**
	 * 페이징처리해서 쪽지 리스트 가져오기
	 * @return
	 */
	@GetMapping("/mail")
	public String getMails(@RequestParam(defaultValue = "1") int page,
			Model model, Principal principal) {
		
        Long memberId = memberService.findByUserId(principal.getName()).getId();
		
		int totalCount = mailService.getMailCountByMemberId(memberId);
	    int totalPages = (int) Math.ceil((double) totalCount / PAGESIZE);
	    int offset = (page - 1) * PAGESIZE;

	    int startPage = ((page - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
	    int endPage = startPage + BLOCK_SIZE - 1;
	    if (endPage > totalPages) {
	        endPage = totalPages;
	    }
		
	    List<MailSummary> mailList = mailService.getPagedMailsSummaryByMemberId(offset, PAGESIZE, memberId);

	    model.addAttribute("mailList", mailList);
	    model.addAttribute("loginId", memberId);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);

	    return "mail/list";
	}
	
	/**
	 * 쪽지 내용 가져오기
	 * @return
	 */
	@GetMapping("/mail/{mailId}")
	public String getMail(@PathVariable("mailId") Long mailId, Model model, Principal principal) {
		
		MailResponse mail = mailService.getMailResponse(mailId);

        Long memberId = memberService.findByUserId(principal.getName()).getId();
		if (memberId != mail.getSenderId() &&
				memberId != mail.getReceiverId()) {
			throw new RuntimeException("잘못된 접근입니다.");
		}
		
		model.addAttribute("mail", mail);
		model.addAttribute("loginId", memberId);
		
		return "mail/detail";
	}
	
	// 새로운 프로세스 요청 시작(거래 요청/구직 신청)
    @PostMapping("/mail/request")
    public String startProcess(@ModelAttribute StartProcessRequest request, 
    		Principal principal, RedirectAttributes rttr) {
        
        Long senderId = memberService.findByUserId(principal.getName()).getId();
        
        Long postId = request.getPostId();
        PostResponse post = postRepository.findById(postId).orElseThrow(
				() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다: " + postId));
        
        if (senderId == post.getAuthorId()) {
        	rttr.addFlashAttribute("errorMessage", "자신이 작성한 게시글에는 요청할 수 없습니다.");
        	return "redirect:/post/" + postId;
        }
        
        mailService.startNewProcess(request, senderId);
        
        rttr.addFlashAttribute("message", "요청이 완료되었습니다.");
        
        return "redirect:/mail"; 
    }
    
    @PostMapping("/mail/action")
    public String processAction(@ModelAttribute ProcessActionRequest request, Principal principal, 
    		RedirectAttributes rttr) {
        
        try {
			Long senderId = memberService.findByUserId(principal.getName()).getId();
			
			mailService.handleProcessAction(request, senderId);
			
			switch (request.getActionType()) {
			    case SET_CONDITIONS:
			        rttr.addFlashAttribute("message", "조건 설정이 완료되었습니다.");
			        break;
			    case ACCEPT_TRADE:
			        rttr.addFlashAttribute("message", "거래가 승인되었습니다.");
			        break;
			    case REJECT_TRADE:
			        rttr.addFlashAttribute("message", "거래가 취소되었습니다.");
			        break;
			    case TRANSFER:
			        rttr.addFlashAttribute("message", "이체 처리되었습니다.");
			        break;
			    case ACCEPT_RECRUIT:
			        rttr.addFlashAttribute("message", "구인 처리가 완료되었습니다.");
			        break;
			    case REJECT_RECRUIT:
			        rttr.addFlashAttribute("message", "구인 거절 처리되었습니다.");
			        break;
			}
			
			return "redirect:/mail";
		} catch (Exception e) {
			
			rttr.addFlashAttribute("error", e.getMessage());
	        return "redirect:/mail/" + request.getMailId();
		}
    }   
}
