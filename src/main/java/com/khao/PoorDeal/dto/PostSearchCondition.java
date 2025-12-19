package com.khao.PoorDeal.dto;

import com.khao.PoorDeal.domain.PostType;

import lombok.Data;


/**
 * @file PostSearchCondition.java
 * @brief 게시물 검색 조건을 담는 DTO 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Data
public class PostSearchCondition {
	
	/** @brief 검색할 게시물 타입 */
    private PostType type;
    /** @brief 검색 키워드 (제목 또는 내용) */
    private String keyword;
}
