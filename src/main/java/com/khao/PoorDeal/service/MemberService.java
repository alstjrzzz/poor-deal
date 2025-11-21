package com.khao.PoorDeal.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	public Member findByUserId(String userId) {
		
		return memberRepository.findByUserId(userId).orElseThrow(
				() -> new UsernameNotFoundException(userId + " 사용자를 찾을 수 없습니다."));
	}
}
