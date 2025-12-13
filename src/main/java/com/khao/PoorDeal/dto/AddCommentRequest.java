package com.khao.PoorDeal.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @file AddCommentRequest.java
 * @brief 댓글 추가 요청 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
public class AddCommentRequest {

	/** @brief 부모 엔티티의 타입 (POST 또는 COMMENT) */
	private String parentType;
	/** @brief 부모 엔티티의 ID */
	private Long parentId;
	/** @brief 댓글 내용 */
	private String content;
}
