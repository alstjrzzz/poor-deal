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

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	
	
	/**
	 * 회원가입 폼
	 * @return register.jsp
	 */
	@GetMapping("/register")
	public String register(Model model) {
		
		model.addAttribute("member", Member.builder().build());
		
		return "register";
	}
	
	/**
	 * 회원가입 후 로그인 페이지로 리다이렉트
	 * @param member
	 * @param rttr
	 * @return home.jsp
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
	 * 로그인 폼
	 * @param model
	 * @return login.jsp
	 */
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	/**
	 * 로그인, 로그아웃은 Spring Security 위임
	 */
}
