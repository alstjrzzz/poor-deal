package com.khao.PoorDeal.dto;


/**
 * @file ActionType.java
 * @brief 클라이언트가 서버에 요청하는 행동의 종류를 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum ActionType {

	// 거래 프로세스 액션
	/** @brief 판매자가 거래 조건을 제시하는 액션. `ProcessStatus`를 `TRADE_CONDITIONS_SET`으로 변경 요청. */
    SET_CONDITIONS,
    /** @brief 구매자가 거래를 최종 수락하는 액션. `ProcessStatus`를 `TRADE_ACCEPTED`으로 변경 요청. */
    ACCEPT_TRADE,
    /** @brief 구매자가 거래를 최종 거절하는 액션. `ProcessStatus`를 `TRADE_REJECTED`으로 변경 요청. */
    REJECT_TRADE,
    /** @brief 구매자가 판매자에게 포인트를 송금하는 액션. */
    TRANSFER,
    
    // 구인/구직 프로세스 액션
    /** @brief 구인자가 지원을 수락하는 액션. `ProcessStatus`를 `RECRUIT_ACCEPTED`으로 변경 요청. */
    ACCEPT_RECRUIT,
    /** @brief 구인자가 지원을 거절하는 액션. `ProcessStatus`를 `RECRUIT_REJECTED`으로 변경 요청. */
    REJECT_RECRUIT;
}
