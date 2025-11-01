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
		
		GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole());
        
        return new User(
            member.getUserId(),
            member.getPassword(),
            Collections.singleton(authority)
        );
	}

	
}
