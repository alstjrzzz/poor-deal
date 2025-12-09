package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Mail {

	private Long id;
	private Long senderId;
	private Long receiverId;
	private Long postId;
	private ProcessType processType;
	private ProcessStatus processStatus;
	private String content;
	private Long amount;
	private LocalDateTime createdAt;
}
