package com.khao.PoorDeal.dto;

import java.time.LocalDateTime;

import com.khao.PoorDeal.domain.PostType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file PostSummary.java
 * @brief 게시물 목록에 표시될 요약 정보를 담는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
public class PostSummary {

	/** @brief 게시물의 고유 ID */
	private Long id;
	/** @brief 게시물 제목 */
	private String title;
	/** @brief 작성자의 이름 */
	private String author;
	/** @brief 게시물 타입 문자열 */
	private String type;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
	/** @brief 마지막 수정 일시 */
	private LocalDateTime updatedAt;
}
