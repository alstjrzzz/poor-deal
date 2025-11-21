package com.khao.PoorDeal.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khao.PoorDeal.domain.Member;
import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void register(Member inputMember) {
		
		Member member = Member.builder()
				.userId(inputMember.getUserId())
				.password(passwordEncoder.encode(inputMember.getPassword()))
				.userName(inputMember.getUserName())
				.email(inputMember.getEmail())
				.role("ROLE_USER")
				.point(0L)
				.build();
		
		memberRepository.save(member);
	}
}
