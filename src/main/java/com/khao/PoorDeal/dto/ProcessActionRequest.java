package com.khao.PoorDeal.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @file ProcessActionRequest.java
 * @brief 프로세스(거래, 구인) 내 특정 액션 요청 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
public class ProcessActionRequest {

	/** @brief 관련된 쪽지의 ID */
	private Long mailId;
	/** @brief 관련된 게시물의 ID */
	private Long postId;
	/** @brief 수행할 액션의 타입 */
    private ActionType actionType;
    /** @brief 액션과 함께 전달할 내용 (예: 거절 사유) */
    private String content;
    /** @brief 거래 시간 (SET_CONDITIONS 액션 시 사용) */
    private String tradeTime; 
    /** @brief 거래 장소 (SET_CONDITIONS 액션 시 사용) */
    private String tradeLocation;
}
