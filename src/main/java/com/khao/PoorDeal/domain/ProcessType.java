package com.khao.PoorDeal.domain;

/**
 * @file ProcessType.java
 * @brief 쪽지와 관련된 프로세스의 종류를 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum ProcessType {

	/** @brief 일반 쪽지 */
	GENERAL(),
	/** @brief 거래 관련 프로세스 */
	TRADE(),
	/** @brief 구인/구직 관련 프로세스 */
	RECRUIT();
}
