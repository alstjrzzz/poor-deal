package com.khao.PoorDeal.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Member;
import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.mapper.MemberMapper;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

	private final MemberMapper memberMapper;
	
	public void save(Member member) {
		memberMapper.save(member);
	}
	
	public Optional<Member> findByUserId(String userId) {
		return memberMapper.findByUserId(userId);
	}
}
