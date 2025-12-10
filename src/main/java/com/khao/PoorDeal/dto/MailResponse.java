package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.ProcessStatus;
import com.khao.PoorDeal.domain.ProcessType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailResponse {

	private Long id;
	private Long senderId;
	private String sender;
	private Long receiverId;
	private String receiver;
	private Long postId;
	private ProcessType processType;
	private ProcessStatus processStatus;
	private String title;
	private String content;
	private Long amount;
	private LocalDateTime createdAt;
}
