package com.khao.PoorDeal.dto;

import com.khao.PoorDeal.domain.PostType;

import lombok.Data;

@Data
public class PostSearchCondition {
	
    private PostType type;
    private String keyword;
}