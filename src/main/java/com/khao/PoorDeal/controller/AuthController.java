package com.khao.PoorDeal.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.domain.Member;

import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.service.AuthService;

/**
 * @file AuthController.java
 * @brief 회원 가입 및 로그인 관련 웹 요청을 처리하는 컨트롤러 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	
	
	/**
	 * @brief 회원가입 폼 페이지를 반환합니다.
	 * @param model 뷰에 전달할 모델
	 * @return "register" 뷰 (register.jsp)
	 */
	@GetMapping("/register")
	public String register(Model model) {
		
		model.addAttribute("member", Member.builder().build());
		
		return "register";
	}
	
	/**
	 * @brief 회원가입 요청을 처리합니다.
	 * @details @Valid를 통해 Member 객체의 유효성을 검사하고, 성공 시 회원을 등록한 후 로그인 페이지로 리다이렉트합니다.
	 * @param member 폼에서 전송된 회원 정보
	 * @param bindingResult 유효성 검사 결과
	 * @return 성공 시 "redirect:/login", 실패 시 "register" 뷰
	 */
	@PostMapping("/register")
	public String register(@Valid Member member, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
            return "register";
        }
		
		authService.register(member);
		
		return "redirect:/login";
	}
	
	/**
	 * @brief 로그인 폼 페이지를 반환합니다.
	 * @return "login" 뷰 (login.jsp)
	 */
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	/**
	 * @brief 로그인 및 로그아웃 처리는 Spring Security에 위임됩니다.
	 * @see com.khao.PoorDeal.config.SecurityConfig
	 */
}
