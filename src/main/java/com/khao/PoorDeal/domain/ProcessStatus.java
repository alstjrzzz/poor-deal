package com.khao.PoorDeal.domain;

public enum ProcessStatus {
	
	TRADE_REQUEST_PENDING(),	// 살래요(구매자가 판매자에게)
	TRADE_CONDITIONS_SET(), 	// 1000원 어떰(판매자가 구매자에게)
	TRADE_ACCEPTED(),			// 좋네요(구매자가 판매자에게)
	TRADE_REJECTED(),			// 싫네요(구매자가 판매자에게)
	TRADE_TRANSFER_PENDING(),	// 1000원 보내줘야 함(판매자가 구매자에게)
	TRADE_PROCESS_COMPLETED(),	// 거래 완료(양쪽에 다 보냄)
	
	RECRUIT_REQUEST_PENDING(),	// 일할래요(구직자가 구인자에게)
	RECRUIT_PROCESS_COMPLETED();// 네(양쪽에 다 보냄)
}
