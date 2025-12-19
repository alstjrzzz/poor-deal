package com.khao.PoorDeal.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

/**
 * @file AddPostRequest.java
 * @brief 게시물 추가 요청 시 사용되는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
public class AddPostRequest {

	/** @brief 게시물 제목 */
	private String title;
	/** @brief 게시물 내용 */
	private String content;
	/** @brief 업로드된 이미지 파일 */
	private MultipartFile image;
	/** @brief 게시물 타입 (FREE, TRADE, JOB) */
	private String type;
	/** @brief 거래 게시물의 가격 */
	private Long price;
	/** @brief 구인 게시물의 채용 인원 */
	private Integer hiringQuota;
}
