package com.khao.PoorDeal.repository;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.mapper.TradeMapper;

import lombok.RequiredArgsConstructor;

/**
 * @file TradeRepository.java
 * @brief 거래 관련 데이터 처리를 위한 레포지토리 클래스입니다. TradeMapper를 사용하여 데이터베이스와 연동합니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Repository
@RequiredArgsConstructor
public class TradeRepository {

	private final TradeMapper tradeMapper;
	
	/** @see com.khao.PoorDeal.mapper.TradeMapper#updateMemberPoint(Long, Long) */
	public void updateMemberPoint(Long memberId, Long point) { tradeMapper.updateMemberPoint(memberId, point); }
}
