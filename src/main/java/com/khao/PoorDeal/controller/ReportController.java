package com.khao.PoorDeal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.domain.MemberRole;
import com.khao.PoorDeal.domain.ReportStatus;
import com.khao.PoorDeal.dto.ReportRequest;
import com.khao.PoorDeal.dto.ReportResponse;
import com.khao.PoorDeal.repository.MemberRepository;
import com.khao.PoorDeal.service.MemberService;
import com.khao.PoorDeal.service.ReportService;

import lombok.RequiredArgsConstructor;

/**
 * @file ReportController.java
 * @brief 사용자 신고 및 관리자 신고 처리 관련 웹 요청을 처리하는 컨트롤러 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * @brief 사용자 신고 폼 페이지를 반환합니다.
     * @param suspectId 피신고자 ID
     * @param postId 신고 대상 게시물 ID (선택 사항)
     * @param model 뷰에 전달할 모델
     * @return "report/reportForm" 뷰
     */
    @GetMapping("/report")
    public String showReportForm(
            @RequestParam("suspectId") Long suspectId,
            @RequestParam(value = "postId", required = false) Long postId,
            Model model) {
        
        Member suspect = memberRepository.findById(suspectId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        model.addAttribute("suspectName", suspect.getUserName());
        
        ReportRequest reportRequest = ReportRequest.builder()
                .suspectId(suspectId)
                .postId(postId)
                .build();
        
        model.addAttribute("reportRequest", reportRequest);
        
        return "report/reportForm";
    }

    /**
     * @brief 사용자 신고 제출을 처리합니다.
     * @param reportRequest 신고 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 메인 페이지("/")로 리다이렉트
     */
    @PostMapping("/report/submit")
    public String submitReport(
            @ModelAttribute ReportRequest reportRequest,
            Principal principal,
            RedirectAttributes rttr) {
        
        Long reporterId = memberService.findByUserId(principal.getName()).getId();
        reportRequest.setReporterId(reporterId);
        
        try {
            reportService.submitReport(reportRequest);
            rttr.addFlashAttribute("message", "🚨 신고가 정상적으로 접수되었습니다. 관리자 검토 후 처리됩니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "신고 접수 중 오류가 발생했습니다.");
        }
        
        return "redirect:/";
    }

    /**
     * @brief (관리자) 전체 신고 목록 페이지를 조회합니다.
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @param model 뷰에 전달할 모델
     * @return "report/reportList" 뷰. 관리자가 아닐 경우 메인 페이지로 리다이렉트.
     */
    @GetMapping("/admin/report/list")
    public String listReports(Principal principal, RedirectAttributes rttr, Model model) {

        Member member = memberService.findByUserId(principal.getName());
        
        if (member.getRole() != MemberRole.ROLE_ADMIN) { 
            rttr.addFlashAttribute("error", "관리자만 접근할 수 있습니다.");
            return "redirect:/"; 
        }

        List<ReportResponse> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        
        return "report/reportList";
    }

    /**
     * @brief (관리자) 신고를 처리(승인/반려)합니다.
     * @param id 처리할 신고 ID
     * @param statusStr 새로운 신고 상태 문자열
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 신고 목록 페이지("/admin/report/list")로 리다이렉트
     */
    @PostMapping("/admin/report/process")
    public String processReport(
            @RequestParam("id") Long id,
            @RequestParam("status") String statusStr,
            RedirectAttributes rttr) {
        
        try {
            ReportStatus status = ReportStatus.valueOf(statusStr);
            reportService.processReport(id, status);
            
            String msg = "신고 처리 완료";
            if(status == ReportStatus.APPROVED) msg += " (승인/제재)";
            else if(status == ReportStatus.REJECTED) msg += " (반려)";
            
            rttr.addFlashAttribute("message", "✅ " + msg + " 되었습니다.");
            
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "redirect:/admin/report/list";
    }
}
