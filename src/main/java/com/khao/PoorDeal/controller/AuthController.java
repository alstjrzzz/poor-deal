package com.khao.PoorDeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.domain.Member;

import lombok.RequiredArgsConstructor;
import com.khao.PoorDeal.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	
	
	/**
	 * 회원가입 폼
	 * @return register.jsp
	 */
	@GetMapping("/register")
	public String register() {
		
		return "register";
	}
	
	/**
	 * 회원가입 후 로그인 페이지로 리다이렉트
	 * @param member
	 * @param rttr
	 * @return home.jsp
	 */
	@PostMapping("/register")
	public String register(Member member, RedirectAttributes rttr) {
		
		authService.register(member);
		
		return "redirect:/login";
	}
	
	/**
	 * 로그인 폼
	 * @param model
	 * @return login.jsp
	 */
	@GetMapping("/login")
	public String login(Model model) {
		
		return "login";
	}
	
	/**
	 * 로그인, 로그아웃은 Spring Security 위임
	 */
}
