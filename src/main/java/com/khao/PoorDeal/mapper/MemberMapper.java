package com.khao.PoorDeal.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Member;

/**
 * @file MemberMapper.java
 * @brief 회원 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Mapper
public interface MemberMapper {

	/**
	 * @brief 새로운 회원을 데이터베이스에 저장합니다.
	 * @param member 저장할 회원 객체
	 */
	public void save(Member member);

	/**
	 * @brief 사용자 ID로 회원을 찾아 반환합니다.
	 * @param userId 사용자 ID
	 * @return Member 객체를 포함하는 Optional 객체
	 */
	public Optional<Member> findByUserId(String userId);

	/**
	 * @brief 고유 ID로 회원을 찾아 반환합니다.
	 * @param id 회원 ID
	 * @return Member 객체를 포함하는 Optional 객체
	 */
	public Optional<Member> findById(Long id);

	/**
	 * @brief 회원의 계정 차단 상태를 업데이트합니다.
	 * @param id 회원 ID
	 * @param isBlock 차단 상태
	 */
	public void updateBlockStatus(@Param("id") Long id, @Param("isBlock") boolean isBlock);
}
