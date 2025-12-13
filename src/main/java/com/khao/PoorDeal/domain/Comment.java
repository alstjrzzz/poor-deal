package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file Comment.java
 * @brief 댓글 정보를 담는 도메인 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */

@Getter
@Setter
@Builder
public class Comment {

	/** @brief 댓글의 고유 ID */
	private Long id;
	/** @brief 부모 엔티티의 타입 (예: POST, COMMENT) */
	private ParentType parentType;
	/** @brief 부모 엔티티의 ID */
	private Long parentId;
	/** @brief 댓글 작성자의 ID */
	private Long authorId;
	/** @brief 댓글 내용 */
	private String content;
	/** @brief 댓글 상태 (예: 활성, 삭제됨) */
	private CommentStatus status;
	/** @brief 마지막 수정 일시 */
	private LocalDateTime updatedAt;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
}
