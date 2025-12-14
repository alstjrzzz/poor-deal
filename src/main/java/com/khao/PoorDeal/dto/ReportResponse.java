package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

	private Long id;
	private String reason;
	private Long reporterId;
	private String reporterName;
	private Long postId;
	private Long suspectId;
	private String suspectName;
	private ReportStatus status;
	private LocalDateTime createdAt;
}
