package com.khao.PoorDeal.dto;

import java.util.List;

import com.khao.PoorDeal.domain.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentAndRepliesResponse {

	private CommentResponse comment;
	private List<CommentResponse> replies;
}
