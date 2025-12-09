package com.khao.PoorDeal.domain;

// 현재 이런 상황이다(서버 내부)
public enum ProcessStatus {
	
	TRADE_REQUEST_PENDING(),	// 살래요(구매자가 판매자에게)
	TRADE_CONDITIONS_SET(), 	// 1000원 어떰(판매자가 구매자에게)
	TRADE_ACCEPTED(),			// 좋네요(구매자가 판매자에게)
	TRADE_REJECTED(),			// 싫네요(구매자가 판매자에게)
	TRADE_TRANSFER_PENDING(),	// 1000원 보내줘야 함(판매자가 구매자에게)
	TRADE_PROCESS_COMPLETED(),	// 거래 완료(양쪽에 다 보냄)
	
	RECRUIT_REQUEST_PENDING(),	// 일할래요(구직자가 구인자에게)
	RECRUIT_ACCEPTED(),         // 수락함(구인자가 구직자에게)
    RECRUIT_REJECTED(),			// 거절함(구인자가 구직자에게)

    PROCESSED();				// 이전 단계의 메일이 처리되어 만료됨
}
