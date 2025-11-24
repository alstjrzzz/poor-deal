package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Comment {

	private Long id;
	private ParentType parentType;
	private Long parentId;
	private Long authorId;
	private String content;
	private LocalDateTime createdAt;
}
