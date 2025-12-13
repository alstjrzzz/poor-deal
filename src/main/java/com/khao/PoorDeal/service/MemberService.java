package com.khao.PoorDeal.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * @file MemberService.java
 * @brief 회원 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	/**
	 * @brief 사용자 ID로 회원 정보를 조회합니다.
	 * @param userId 조회할 사용자 ID
	 * @return Member 도메인 객체
	 * @throws UsernameNotFoundException 해당 사용자를 찾을 수 없을 때 발생
	 */
	public Member findByUserId(String userId) {
		
		return memberRepository.findByUserId(userId).orElseThrow(
				() -> new UsernameNotFoundException(userId + " 사용자를 찾을 수 없습니다."));
	}
}
