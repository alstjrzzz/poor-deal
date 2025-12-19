package com.khao.PoorDeal.domain;

/**
 * @file ProcessStatus.java
 * @brief 거래 및 구인 프로세스의 내부 상태를 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum ProcessStatus {
	
	/** @brief 구매자가 판매자에게 구매 요청을 보낸 상태 */
	TRADE_REQUEST_PENDING(),
	/** @brief 판매자가 구매자에게 거래 조건을 설정하여 보낸 상태 */
	TRADE_CONDITIONS_SET(),
	/** @brief 구매자가 판매자의 거래 조건을 수락한 상태 */
	TRADE_ACCEPTED(),
	/** @brief 구매자가 판매자의 거래 조건을 거절한 상태 */
	TRADE_REJECTED(),
	/** @brief 구매자가 판매자에게 금액을 송금해야 하는 상태 */
	TRADE_TRANSFER_PENDING(),
	/** @brief 거래의 모든 과정이 완료된 상태 */
	TRADE_PROCESS_COMPLETED(),
	
	/** @brief 구직자가 구인자에게 지원 요청을 보낸 상태 */
	RECRUIT_REQUEST_PENDING(),
	/** @brief 구인자가 구직자의 지원을 수락한 상태 */
	RECRUIT_ACCEPTED(),
	/** @brief 구인자가 구직자의 지원을 거절한 상태 */
    RECRUIT_REJECTED(),

    /** @brief 이전 단계의 쪽지가 처리되어 만료된 상태 */
    PROCESSED();
}
