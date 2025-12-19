package com.khao.PoorDeal.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khao.PoorDeal.domain.Member;
import com.khao.PoorDeal.domain.MemberRole;

import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.repository.MemberRepository;

/**
 * @file AuthService.java
 * @brief 회원 가입 등 인증 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * @brief 신규 회원을 등록합니다.
	 * @details 비밀번호를 암호화하고 기본 역할을 'ROLE_USER'로 설정하여 저장합니다.
	 * @param inputMember 회원 가입 폼에서 입력된 회원 정보
	 */
	public void register(Member inputMember) {

        Member member = Member.builder()
                .userId(inputMember.getUserId())
                .password(passwordEncoder.encode(inputMember.getPassword()))
                .userName(inputMember.getUserName())
                .email(inputMember.getEmail())
                .role(MemberRole.ROLE_USER)
                .point(0L)
                .isBlock(false)
                .build();

        memberRepository.save(member);
    }
}
