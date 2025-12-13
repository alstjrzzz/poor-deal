package com.khao.PoorDeal.domain;

/**
 * @file ParentType.java
 * @brief 댓글의 부모 타입을 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum ParentType {

	/** @brief 부모가 게시물인 경우 */
	POST(),
	/** @brief 부모가 다른 댓글인 경우 (대댓글) */
	COMMENT();
}
