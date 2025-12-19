package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.ProcessStatus;
import com.khao.PoorDeal.domain.ProcessType;

import lombok.Getter;
import lombok.Setter;

/**
 * @file StartProcessRequest.java
 * @brief 새로운 프로세스(거래, 구인) 시작 요청 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Setter
@Getter
public class StartProcessRequest {

	/** @brief 수신자의 ID */
	private Long receiverId;
	/** @brief 관련된 게시물의 ID */
	private Long postId;
	/** @brief 시작할 프로세스의 타입 */
	private ProcessType processType;
	/** @brief 요청과 함께 전달할 내용 */
	private String content;
	/** @brief 제안할 금액 (거래 시) */
	private Long amount;
}
