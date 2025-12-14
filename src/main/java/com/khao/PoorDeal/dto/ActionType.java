package com.khao.PoorDeal.dto;

// 이걸 해주세요(클라이언트 -> 서버 요청)
public enum ActionType {

	// 거래 프로세스 액션
    SET_CONDITIONS,       // 판매자가 조건 제시 → TRADE_CONDITIONS_SET
    ACCEPT_TRADE,         // 구매자가 최종 수락 → TRADE_ACCEPTED
    REJECT_TRADE,         // 구매자가 최종 거절 → TRADE_REJECTED
    TRANSFER,		 	  // 구매자가 판매자에게 송금 
    
    // 구인/구직 프로세스 액션
    ACCEPT_RECRUIT,       // 구인자가 수락 → RECRUIT_PROCESS_COMPLETED
    REJECT_RECRUIT;		  // 구인자가 거절
}