package com.khao.PoorDeal.domain;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @file Post.java
 * @brief 게시물 정보를 담는 도메인 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
public class Post {

	/** @brief 게시물의 고유 ID */
	private Long id;
	/** @brief 게시물 제목 */
	private String title;
	/** @brief 게시물 내용 */
	private String content;
	/** @brief 작성자의 ID */
	private Long authorId;
	/** @brief 이미지 파일 경로 또는 URL */
	private String image;
	/** @brief 게시물 타입 (예: 자유, 거래, 구인) */
	private PostType type;
	/** @brief 생성 일시 */
	private LocalDateTime createdAt;
	/** @brief 마지막 수정 일시 */
	private LocalDateTime updatedAt;
	/** @brief 거래 또는 구인 가능 여부 */
	private boolean isAvailable;
	/** @brief 삭제 여부 */
	private boolean isDeleted;
	
	/** @brief 거래 게시물의 가격 */
	private Long price;
	
	/** @brief 구인 게시물의 채용 인원 */
	private int hiringQuota;
	/** @brief 현재까지 채용된 인원 */
	private int filledCount;
}
