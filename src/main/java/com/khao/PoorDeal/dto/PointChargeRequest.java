package com.khao.PoorDeal.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @file PointChargeRequest.java
 * @brief 포인트 충전 요청 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
public class PointChargeRequest {

	/** @brief 충전할 포인트 금액 */
	private Long amount;
}
