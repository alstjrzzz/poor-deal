package com.khao.PoorDeal.service;

import java.util.Collections;
import java.util.NoSuchElementException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.khao.PoorDeal.domain.Member;
import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.repository.MemberRepository;

/**
 * @file AuthDetailsService.java
 * @brief Spring Security의 UserDetailsService를 구현하여 사용자 인증 정보를 로드하는 서비스 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	
	/**
     * @brief 사용자 ID(username)를 기반으로 사용자 정보를 조회하여 UserDetails 객체를 반환합니다.
     * @param userId Spring Security에서 username으로 사용되는 사용자 ID
     * @return UserDetails Spring Security가 사용하는 사용자 상세 정보 객체
     * @throws UsernameNotFoundException 해당 userId의 사용자를 찾을 수 없을 때 발생
     */
	@Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new UsernameNotFoundException(userId + " 사용자를 찾을 수 없습니다."));
        
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
        
        return new User(
            member.getUserId(),
            member.getPassword(),
            true,  // enabled (계정 활성화 여부)
            true,  // accountNonExpired (계정 만료 여부)
            true,  // credentialsNonExpired (비밀번호 만료 여부)
            !member.isBlock(), // accountNonLocked (계정 잠금 여부) -> isBlock이 true면 false(잠김)가 되어야 함
            Collections.singleton(authority)
        );
    }
}
