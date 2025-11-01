package com.khao.PoorDeal.domain;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Member {

	private Long id;
	
	@NotEmpty(message = "아이디는 필수 입력 값입니다.")
	private String userId;
	
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
             message = "비밀번호는 8자 이상이며, 영문/숫자/특수문자를 포함해야 합니다.")
	private String password;
	
	@NotEmpty(message = "이름은 필수 입력 값입니다.")
	private String userName;
	
	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	
	private String role;
}
