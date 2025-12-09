package com.khao.PoorDeal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessActionRequest {

	private Long mailId;
	private Long postId;
    private ActionType actionType; // actionType 참고, service에서 processStatus와 비교하여 로직 수행
    private String content;
    // TRADE_CONDITIONS_SET 등의 액션 시 필요한 추가 정보
    private String tradeTime; 
    private String tradeLocation;
}
