package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.domain.PostType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostResponse {

	private Long id;
	private String title;
	private String content;
	private Long authorId;
	private String image;
	private PostType type;
	private LocalDateTime createdAt;
	private boolean isAvailable;
	
	// trade
	private Long price;
	
	// job
	private Integer hiringQuota;
	private Integer filledCount;
	
	private String author;
}
