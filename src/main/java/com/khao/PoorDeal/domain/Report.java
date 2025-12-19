package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @file Report.java
 * @brief 사용자 신고 정보를 담는 도메인 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

	/** @brief 신고의 고유 ID */
	private Long id;
	/** @brief 신고 사유 */
	private String reason;
	/** @brief 신고자의 ID */
	private Long reporterId;
	/** @brief 신고된 게시물의 ID */
	private Long postId;
	/** @brief 피신고자의 ID */
	private Long suspectId;
	/** @brief 신고 처리 상태 */
	private ReportStatus status;
	/** @brief 신고 접수 일시 */
	private LocalDateTime createdAt;
}
