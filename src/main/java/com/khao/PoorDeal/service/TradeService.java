package com.khao.PoorDeal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.repository.MemberRepository;
import com.khao.PoorDeal.repository.TradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final TradeRepository tradeRepository;
	private final MemberRepository memberRepository;
	
	@Transactional
	public void chargePoint(Long memberId, Integer amount) {
		
		if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        
        Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
                            
        tradeRepository.updateMemberPoint(memberId, member.getPoint() + amount);
	}
}
