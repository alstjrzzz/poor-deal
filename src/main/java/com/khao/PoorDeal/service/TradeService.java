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
	
	@Transactional
	public boolean tradePoint(Long senderId, Long receiverId, Long amount) {
		
		if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("유효한 금액(0 초과)을 입력해야 합니다.");
        }
		
		Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("보내는 사람을 찾을 수 없습니다."));
	        
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("받는 사람을 찾을 수 없습니다."));

        if (sender.getPoint() < amount) {
            throw new RuntimeException("잔액이 부족하여 포인트 이체에 실패했습니다. 현재 잔액: " + sender.getPoint());
        }

        sender.setPoint(sender.getPoint() - amount);
        receiver.setPoint(receiver.getPoint() + amount);
        
        tradeRepository.updateMemberPoint(senderId, sender.getPoint());
        tradeRepository.updateMemberPoint(receiverId, receiver.getPoint());
        
        return true;
	}
}
