package com.khao.PoorDeal.domain;

/**
 * @file PostType.java
 * @brief 게시물의 종류를 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum PostType {

	/** @brief 자유 게시판 */
	FREE(),
	/** @brief 중고 거래 게시판 */
	TRADE(),
	/** @brief 구인/구직 게시판 */
	JOB();
}
