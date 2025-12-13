package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file Mail.java
 * @brief 사용자 간의 메시지(쪽지) 정보를 담는 도메인 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
public class Mail {

	/** @brief 쪽지의 고유 ID */
	private Long id;
	/** @brief 발신자의 ID */
	private Long senderId;
	/** @brief 수신자의 ID */
	private Long receiverId;
	/** @brief 관련된 게시물의 ID */
	private Long postId;
	/** @brief 프로세스 타입 (예: 일반, 거래, 구인) */
	private ProcessType processType;
	/** @brief 프로세스 상태 */
	private ProcessStatus processStatus;
	/** @brief 쪽지 제목 */
	private String title;
	/** @brief 쪽지 내용 */
	private String content;
	/** @brief 거래와 관련된 금액 */
	private Long amount;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
}
