package com.khao.PoorDeal.domain;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @file Member.java
 * @brief 회원 정보를 담는 도메인 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	/** @brief 회원의 고유 ID */
	private Long id;
	
	/** @brief 사용자 ID (로그인 시 사용) */
	@NotEmpty(message = "아이디는 필수 입력 값입니다.")
	private String userId;
	
	/** @brief 비밀번호 */
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$",
             message = "비밀번호는 8자 이상이며, 영문/숫자/특수문자를 포함해야 합니다.")
	private String password;
	
	/** @brief 사용자 이름 */
	@NotEmpty(message = "이름은 필수 입력 값입니다.")
	private String userName;
	
	/** @brief 이메일 주소 */
	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;
	
	/** @brief 보유 포인트 */
	private Long point;
	
	/** @brief 회원 권한 */
	private MemberRole role;
	
	/** @brief 계정 차단 여부 */
	private boolean isBlock;
}
