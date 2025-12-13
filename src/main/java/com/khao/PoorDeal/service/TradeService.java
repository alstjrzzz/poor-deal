package com.khao.PoorDeal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.repository.MemberRepository;
import com.khao.PoorDeal.repository.TradeRepository;

import lombok.RequiredArgsConstructor;

/**
 * @file TradeService.java
 * @brief 포인트 충전 및 사용자 간 포인트 거래 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class TradeService {

	private final TradeRepository tradeRepository;
	private final MemberRepository memberRepository;
	
	/**
	 * @brief 특정 회원의 포인트를 충전합니다.
	 * @param memberId 포인트를 충전할 회원 ID
	 * @param amount 충전할 금액
	 * @throws IllegalArgumentException 충전 금액이 0 이하일 경우 발생
	 */
	@Transactional
	public void chargePoint(Long memberId, Long amount) {
		
		if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        
        Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
                            
        tradeRepository.updateMemberPoint(memberId, member.getPoint() + amount);
	}
	
	/**
	 * @brief 한 회원에서 다른 회원으로 포인트를 이체합니다. (거래)
	 * @param senderId 포인트를 보내는 회원 ID
	 * @param receiverId 포인트를 받는 회원 ID
	 * @param amount 이체할 금액
	 * @return 성공 시 true, 실패 시 예외 발생
	 * @throws IllegalArgumentException 금액이 유효하지 않거나, 회원을 찾을 수 없을 때 발생
	 * @throws RuntimeException 보내는 사람의 잔액이 부족할 때 발생
	 */
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
