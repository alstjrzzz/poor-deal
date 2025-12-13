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

@Controller
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;
    private final MemberService memberService;
    
    /**
     * 충전 폼 페이지 표시
     */
    @GetMapping("/trade/charge")
    public String showChargeForm() { 
        return "trade/charge";
    }

    /**
     * 포인트 충전 처리
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