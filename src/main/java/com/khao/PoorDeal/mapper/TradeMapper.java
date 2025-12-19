package com.khao.PoorDeal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @file TradeMapper.java
 * @brief 거래 관련 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Mapper
public interface TradeMapper {

	/**
	 * @brief 특정 회원의 포인트를 업데이트합니다.
	 * @param memberId 포인트를 업데이트할 회원 ID
	 * @param point 변경될 포인트 값
	 */
	public void updateMemberPoint(@Param("memberId") Long memberId, @Param("point") Long point);
}