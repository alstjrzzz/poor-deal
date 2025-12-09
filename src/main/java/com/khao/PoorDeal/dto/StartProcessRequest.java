package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.ProcessStatus;
import com.khao.PoorDeal.domain.ProcessType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StartProcessRequest {

	private Long receiverId;
	private Long postId;
	private ProcessType processType;
	private String content;
	private Long amount;
}
