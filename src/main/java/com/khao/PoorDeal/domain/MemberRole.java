package com.khao.PoorDeal.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    ROLE_USER("일반 사용자"),
    ROLE_ADMIN("관리자");

    private final String description;
}