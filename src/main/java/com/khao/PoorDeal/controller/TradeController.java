package com.khao.PoorDeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TradeController {

	@GetMapping("/")
	public String home() {
		
		return "/home";
	}
}
