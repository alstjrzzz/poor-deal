package com.khao.PoorDeal.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * @file MemberRole.java
 * @brief 회원의 역할을 정의하는 열거형 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@RequiredArgsConstructor
public enum MemberRole {

    /** @brief 일반 사용자 */
    ROLE_USER("일반 사용자"),
    /** @brief 관리자 */
    ROLE_ADMIN("관리자");

    /** @brief 역할에 대한 설명 */
    private final String description;
}
