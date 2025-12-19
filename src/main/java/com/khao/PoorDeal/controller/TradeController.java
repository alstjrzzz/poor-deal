package com.khao.PoorDeal.controller;

import java.security.Principal;
import java.text.DecimalFormat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.dto.PointChargeRequest;
import com.khao.PoorDeal.service.MemberService;
import com.khao.PoorDeal.service.TradeService;

import lombok.RequiredArgsConstructor;

/**
 * @file TradeController.java
 * @brief 포인트 충전 등 거래 관련 웹 요청을 처리하는 컨트롤러 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Controller
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;
    private final MemberService memberService;
    
    /**
     * @brief 포인트 충전 폼 페이지를 반환합니다.
     * @return "trade/charge" 뷰
     */
    @GetMapping("/trade/charge")
    public String showChargeForm() { 
        return "trade/charge";
    }

    /**
     * @brief 포인트 충전 요청을 처리합니다.
     * @param request 포인트 충전 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 성공 시 메인 페이지("/")로, 실패 시 충전 폼으로 리다이렉트
     */
    @PostMapping("/trade/charge")
    public String chargePoint(@ModelAttribute PointChargeRequest request, 
            Principal principal, RedirectAttributes rttr) {

        Long memberId = memberService.findByUserId(principal.getName()).getId();
        
        try {
            tradeService.chargePoint(memberId, request.getAmount());
            
            DecimalFormat decFormat = new DecimalFormat("###,###");
            String formattedAmount = decFormat.format(request.getAmount());

            rttr.addFlashAttribute("message", "⚡ " + formattedAmount + " 포인트 충전이 완료되었습니다!");
            
            return "redirect:/";
            
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "충전 실패: " + e.getMessage());
            return "redirect:/trade/charge";
        }
    }
}
