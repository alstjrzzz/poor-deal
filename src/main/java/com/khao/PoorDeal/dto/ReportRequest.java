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
 * @file ReportRequest.java
 * @brief 사용자 신고 요청 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

	/** @brief 신고 사유 */
	private String reason;
	/** @brief 신고자의 ID */
	private Long reporterId;
	/** @brief 신고된 게시물의 ID */
	private Long postId;
	/** @brief 피신고자의 ID */
	private Long suspectId;
}
