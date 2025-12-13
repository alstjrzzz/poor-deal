package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @file ReportResponse.java
 * @brief 신고 정보 응답 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

	/** @brief 신고의 고유 ID */
	private Long id;
	/** @brief 신고 사유 */
	private String reason;
	/** @brief 신고자의 ID */
	private Long reporterId;
	/** @brief 신고자의 이름 */
	private String reporterName;
	/** @brief 신고된 게시물의 ID */
	private Long postId;
	/** @brief 피신고자의 ID */
	private Long suspectId;
	/** @brief 피신고자의 이름 */
	private String suspectName;
	/** @brief 신고 처리 상태 */
	private ReportStatus status;
	/** @brief 신고 접수 일시 */
	private LocalDateTime createdAt;
}
