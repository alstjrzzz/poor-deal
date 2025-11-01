package com.khao.PoorDeal.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.khao.PoorDeal.domain.Member;

@Mapper
public interface MemberMapper {

	public void save(Member member);
	public Optional<Member> findByUserId(String userId);
}
