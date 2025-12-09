package com.khao.PoorDeal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TradeMapper {

	public void updateMemberPoint(@Param("memberId") Long memberId, @Param("point") Long point);
}