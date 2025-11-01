package com.khao.PoorDeal.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Member {

	private Long id;
	private String userId;
	private String password;
	private String userName;
	private String email;
	private String role;
}
