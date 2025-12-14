package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

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
public class Report {

	private Long id;
	private String reason;
	private Long reporterId;
	private Long postId;
	private Long suspectId;
	private ReportStatus status;
	private LocalDateTime createdAt;
}
