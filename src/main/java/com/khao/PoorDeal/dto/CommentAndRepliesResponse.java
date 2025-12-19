package com.khao.PoorDeal.dto;

import java.util.List;

import com.khao.PoorDeal.domain.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file CommentAndRepliesResponse.java
 * @brief 특정 댓글과 그에 대한 대댓글 목록을 함께 반환하는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
public class CommentAndRepliesResponse {

	/** @brief 원본 댓글 정보 */
	private CommentResponse comment;
	/** @brief 대댓글 목록 */
	private List<CommentResponse> replies;
}
