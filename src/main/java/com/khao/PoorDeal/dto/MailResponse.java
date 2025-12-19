package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.ProcessStatus;
import com.khao.PoorDeal.domain.ProcessType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file MailResponse.java
 * @brief 쪽지 정보 응답 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
public class MailResponse {

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
	/** @brief 관련된 게시물의 ID */
	private Long postId;
	/** @brief 프로세스 타입 */
	private ProcessType processType;
	/** @brief 프로세스 상태 */
	private ProcessStatus processStatus;
	/** @brief 쪽지 제목 */
	private String title;
	/** @brief 쪽지 내용 */
	private String content;
	/** @brief 관련 금액 */
	private Long amount;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
}
