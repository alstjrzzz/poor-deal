package com.khao.PoorDeal.repository;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.mapper.TradeMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TradeRepository {

	private final TradeMapper tradeMapper;
	
	public void updateMemberPoint(Long memberId, Long point) { tradeMapper.updateMemberPoint(memberId, point); }
}
