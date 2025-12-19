package com.khao.PoorDeal.controller;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.ProcessActionRequest;
import com.khao.PoorDeal.dto.StartProcessRequest;
import com.khao.PoorDeal.repository.PostRepository;
import com.khao.PoorDeal.service.MailService;
import com.khao.PoorDeal.service.MemberService;

import lombok.RequiredArgsConstructor;

/**
 * @file MailController.java
 * @brief 쪽지(메시지) 관련 웹 요청을 처리하는 컨트롤러 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Controller
@RequiredArgsConstructor
public class MailController {

    private int PAGESIZE = 10;
    private int BLOCK_SIZE = 10;
    
    private final MailService mailService;
    private final MemberService memberService;
    private final PostRepository postRepository;
    
    /**
     * @brief 현재 로그인한 사용자의 쪽지 목록을 페이징하여 조회합니다.
     * @param page 요청하는 페이지 번호 (기본값 1)
     * @param model 뷰에 전달할 모델
     * @param principal 현재 로그인한 사용자 정보
     * @return "mail/list" 뷰
     */
    @GetMapping("/mail")
    public String getMails(@RequestParam(defaultValue = "1") int page,
            Model model, Principal principal) {
        
        Long memberId = memberService.findByUserId(principal.getName()).getId();
        
        int totalCount = mailService.getMailCountByMemberId(memberId);
        int totalPages = (int) Math.ceil((double) totalCount / PAGESIZE);
        if (totalPages == 0) totalPages = 1;

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
     * @brief 특정 쪽지의 상세 내용을 조회합니다.
     * @param mailId 조회할 쪽지 ID
     * @param model 뷰에 전달할 모델
     * @param principal 현재 로그인한 사용자 정보
     * @return "mail/detail" 뷰
     * @throws RuntimeException 쪽지 수신/발신자가 아닌 경우 발생
     */
    @GetMapping("/mail/{mailId}")
    public String getMail(@PathVariable("mailId") Long mailId, Model model, Principal principal) {
        
        MailResponse mail = mailService.getMailResponse(mailId);

        Long memberId = memberService.findByUserId(principal.getName()).getId();
        if (memberId != mail.getSenderId() && memberId != mail.getReceiverId()) {
            throw new RuntimeException("잘못된 접근입니다.");
        }
        
        model.addAttribute("mail", mail);
        model.addAttribute("loginId", memberId);
        
        return "mail/detail";
    }
    
    /**
     * @brief 새로운 프로세스(거래, 구인)를 시작하는 요청을 처리합니다.
     * @param request 프로세스 시작 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 성공 시 "/mail"로 리다이렉트, 실패 시 게시물 상세 페이지로 리다이렉트
     */
    @PostMapping("/mail/request")
    public String startProcess(@ModelAttribute StartProcessRequest request, 
            Principal principal, RedirectAttributes rttr) {
        
        Long senderId = memberService.findByUserId(principal.getName()).getId();
        
        Long postId = request.getPostId();
        PostResponse post = postRepository.findById(postId).orElseThrow(
                () -> new NoSuchElementException("해당 게시글이 존재하지 않습니다: " + postId));
        
        if (senderId == post.getAuthorId()) {
            rttr.addFlashAttribute("error", "자신이 작성한 게시글에는 요청할 수 없습니다.");
            return "redirect:/post/" + postId;
        }
        
        try {
            mailService.startNewProcess(request, senderId);
            rttr.addFlashAttribute("message", "요청이 성공적으로 전송되었습니다! 🚀");
            return "redirect:/mail"; 
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "요청 실패: " + e.getMessage());
            return "redirect:/post/" + postId;
        }
    }
    
    /**
     * @brief 프로세스 내에서 사용자의 액션(수락, 거절 등)을 처리합니다.
     * @param request 프로세스 액션 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 성공 시 "/mail"로 리다이렉트, 실패 시 쪽지 상세 페이지로 리다이렉트
     */
    @PostMapping("/mail/action")
    public String processAction(@ModelAttribute ProcessActionRequest request, Principal principal, 
            RedirectAttributes rttr) {
        
        try {
            Long senderId = memberService.findByUserId(principal.getName()).getId();
            
            mailService.handleProcessAction(request, senderId);
            
            switch (request.getActionType()) {
                case SET_CONDITIONS:
                    rttr.addFlashAttribute("message", "📝 거래 조건 설정이 완료되었습니다.");
                    break;
                case ACCEPT_TRADE:
                    rttr.addFlashAttribute("message", "🤝 거래가 승인되었습니다.");
                    break;
                case REJECT_TRADE:
                    rttr.addFlashAttribute("message", "🚫 거래가 취소되었습니다.");
                    break;
                case TRANSFER:
                    rttr.addFlashAttribute("message", "💸 이체 처리가 완료되었습니다.");
                    break;
                case ACCEPT_RECRUIT:
                    rttr.addFlashAttribute("message", "🎉 구인(채용) 처리가 완료되었습니다.");
                    break;
                case REJECT_RECRUIT:
                    rttr.addFlashAttribute("message", "🚫 구인 거절 처리가 완료되었습니다.");
                    break;
                default:
                    rttr.addFlashAttribute("message", "작업이 완료되었습니다.");
            }
            
            return "redirect:/mail";
            
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "오류 발생: " + e.getMessage());
            return "redirect:/mail/" + request.getMailId();
        }
    }    
}
