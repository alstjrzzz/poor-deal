package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.CommentStatus;
import com.khao.PoorDeal.domain.ParentType;

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
public class CommentResponse {

	private Long id;
	private ParentType parentType;
	private Long parentId;
	private Long authorId;
	private String author;
	private String content;
	private CommentStatus status;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	
}
