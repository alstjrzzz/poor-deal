package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file MailSummary.java
 * @brief 쪽지 목록에 표시될 요약 정보를 담는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
public class MailSummary {

	/** @brief 쪽지의 고유 ID */
	private Long id;
	/** @brief 발신자의 ID */
	private Long senderId;
	/** @brief 발신자의 이름 */
	private String sender;
	/** @brief 수신자의 ID */
	private Long receiverId;
	/** @brief 수신자의 이름 */
	private String receiver;
	/** @brief 프로세스 타입 문자열 */
	private String processType;
	/** @brief 쪽지 제목 */
	private String title;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
}
