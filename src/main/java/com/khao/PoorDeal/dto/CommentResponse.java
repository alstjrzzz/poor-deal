package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.ParentType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentResponse {

	private Long id;
	private ParentType parentType;
	private Long parentId;
	private Long authorId;
	private String content;
	private LocalDateTime createdAt;
	
	private String author;
}
