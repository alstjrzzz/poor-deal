package com.khao.PoorDeal.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.service.MemberService;
import com.khao.PoorDeal.service.ReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;
	private final MemberService memberService;
	
	/*
	// 1. 신고 폼 페이지 이동 (GET)
    // 예: /report/form?suspectId=5&postId=10
    @GetMapping("/form")
    public String showReportForm(
            @RequestParam("suspectId") Long suspectId,
            @RequestParam(value = "postId", required = false) Long postId,
            Model model) {
        
        model.addAttribute("suspectId", suspectId);
        model.addAttribute("postId", postId);
        return "report/reportForm";
    }

    // 2. 신고 제출 처리 (POST)
    @PostMapping("/submit")
    public String submitReport(
            @ModelAttribute Report report,
            Principal principal) {
        
        // 로그인한 신고자 ID 설정
        Long reporterId = memberService.findByUserId(principal.getName()).getId();
        report.setReporterId(reporterId);
        
        reportService.submitReport(report);
        
        return "redirect:/"; // 완료 후 메인이나 이전 페이지로 리다이렉트
    }

    // --- 관리자 기능 (AdminController로 분리하는 것이 좋음) ---

    // 3. 신고 관리 페이지 (목록)
    @GetMapping("/admin/list")
    public String listReports(Principal principal, RedirectAttributes rttr, Model model) {

        Member member = memberService.findByUserId(principal.getName());
    	
        if (!"ADMIN".equals(member.getRole().toString())) { 
            
        	rttr.addFlashAttribute("errorMessage", "관리자만 접근할 수 있습니다.");
            return "redirect:/"; 
        }

        model.addAttribute("reports", reportService.getAllReports());
        return "admin/reportList";
    }

    // 4. 신고 처리 (승인/반려)
    @PostMapping("/admin/process")
    public String processReport(
            @RequestParam("id") Long id,
            @RequestParam("status") String statusStr) {
        
        ReportStatus status = ReportStatus.valueOf(statusStr); // 문자열 -> Enum 변환
        reportService.processReport(id, status);
        
        return "redirect:/report/admin/list";
    }
    
    // 5. 자기가 한 신고 보기
	 */
}
