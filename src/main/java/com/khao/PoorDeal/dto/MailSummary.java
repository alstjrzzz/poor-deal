package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailSummary {

	private Long id;
	private Long senderId;
	private String sender;
	private Long receiverId;
	private String receiver;
	private String processType;
	private String title;
	private LocalDateTime createdAt;
}
