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

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * 신고 페이지 이동
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
     * 신고 처리
     */
    @PostMapping("/report/submit")
    public String submitReport(
            @ModelAttribute ReportRequest reportRequest,
            Principal principal) {
        
        Long reporterId = memberService.findByUserId(principal.getName()).getId();
        reportRequest.setReporterId(reporterId);
        
        reportService.submitReport(reportRequest);
        
        return "redirect:/";
    }

    // --- 관리자 기능 ---

    /**
     * 신고 관리 페이지
     */
    @GetMapping("/admin/report/list")
    public String listReports(Principal principal, RedirectAttributes rttr, Model model) {

        Member member = memberService.findByUserId(principal.getName());
        
        // [수정] 문자열 비교 -> Enum 타입 비교
        // member.getRole()은 이제 MemberRole 타입을 반환합니다.
        if (member.getRole() != MemberRole.ROLE_ADMIN) { 
            rttr.addFlashAttribute("errorMessage", "관리자만 접근할 수 있습니다.");
            return "redirect:/"; 
        }

        List<ReportResponse> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        
        return "report/reportList";
    }

    /**
     * 신고 처리 (승인/반려)
     */
    @PostMapping("/admin/report/process")
    public String processReport(
            @RequestParam("id") Long id,
            @RequestParam("status") String statusStr) {
        
        ReportStatus status = ReportStatus.valueOf(statusStr);
        reportService.processReport(id, status);
        
        return "redirect:/admin/report/list";
    }
}
