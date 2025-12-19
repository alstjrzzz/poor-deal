package com.khao.PoorDeal.domain;

/**
 * @file ReportStatus.java
 * @brief 신고 처리 상태를 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
public enum ReportStatus {

	/** @brief 접수 대기 중 */
	PENDING(),
	/** @brief 신고가 승인되어 처리됨 (예: 계정 정지) */
    APPROVED(),
    /** @brief 신고가 반려됨 */
    REJECTED(),
    /** @brief 이미 접수된 신고와 중복됨 */
    DUPLICATE()
}
