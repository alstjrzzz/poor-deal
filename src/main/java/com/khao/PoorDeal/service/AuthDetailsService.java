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

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	
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
