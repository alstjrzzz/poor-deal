package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.CommentStatus;
import com.khao.PoorDeal.domain.ParentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @file CommentResponse.java
 * @brief 댓글 정보 응답 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

	/** @brief 댓글의 고유 ID */
	private Long id;
	/** @brief 부모 엔티티의 타입 */
	private ParentType parentType;
	/** @brief 부모 엔티티의 ID */
	private Long parentId;
	/** @brief 작성자의 ID */
	private Long authorId;
	/** @brief 작성자의 이름 */
	private String author;
	/** @brief 댓글 내용 */
	private String content;
	/** @brief 댓글 상태 */
	private CommentStatus status;
	/** @brief 마지막 수정 일시 */
	private LocalDateTime updatedAt;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
	
}
