package com.khao.PoorDeal.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPostRequest {

	private String title;
	private String content;
	private MultipartFile image;
	private String type;
	private Long price;
	private int hiringQuota;
}
