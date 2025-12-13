package com.khao.PoorDeal.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Member;
import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.mapper.MemberMapper;

/**
 * @file MemberRepository.java
 * @brief 회원 데이터 처리를 위한 레포지토리 클래스입니다. MemberMapper를 사용하여 데이터베이스와 연동합니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Repository
@RequiredArgsConstructor
public class MemberRepository {

	private final MemberMapper memberMapper;
	
	/** @see com.khao.PoorDeal.mapper.MemberMapper#save(Member) */
	public void save(Member member) {
		memberMapper.save(member);
	}
	
	/** @see com.khao.PoorDeal.mapper.MemberMapper#findByUserId(String) */
	public Optional<Member> findByUserId(String userId) {
		return memberMapper.findByUserId(userId);
	}
	
	/** @see com.khao.PoorDeal.mapper.MemberMapper#findById(Long) */
	public Optional<Member> findById(Long id) {
		return memberMapper.findById(id);
	}
	
	/** @see com.khao.PoorDeal.mapper.MemberMapper#updateBlockStatus(Long, boolean) */
	public void updateBlockStatus(Long id, boolean isBlock) {
        memberMapper.updateBlockStatus(id, isBlock);
    }
}
