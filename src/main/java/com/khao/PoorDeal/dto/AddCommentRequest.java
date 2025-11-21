package com.khao.PoorDeal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {

	private String parentType;
	private Long parentId;
	private String content;
}
