package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Post {

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
	private int hiringQuota;
	private int filledCount;
}
