package com.khao.PoorDeal.domain;

public enum ReportStatus {

	PENDING(),		// 접수 대기
    APPROVED(),		// 처리됨(정지 처리)
    REJECTED(),		// 반려됨(무죄)
    DUPLICATE()     // 중복됨
}
