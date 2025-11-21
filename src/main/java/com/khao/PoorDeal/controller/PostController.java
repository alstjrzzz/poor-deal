package com.khao.PoorDeal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khao.PoorDeal.dto.AddCommentRequest;
import com.khao.PoorDeal.dto.AddPostRequest;
import com.khao.PoorDeal.dto.CommentAndRepliesResponse;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.service.MemberService;
import com.khao.PoorDeal.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {

	@Value("${post.page.size:10}")
	private int PAGESIZE = 10;
	
	@Value("${post.page.block_size:10}")
	private int BLOCK_SIZE = 10;
	
	private final PostService postService;
	private final MemberService memberService;
	
	/**
	 * 페이징처리해서 게시글 리스트 가져오기
	 * @return
	 */
	@GetMapping("/")
	public String getPosts(@RequestParam(defaultValue = "1") int page, Model model) {
		
		int totalCount = postService.getPostCount();
	    int totalPages = (int) Math.ceil((double) totalCount / PAGESIZE);
	    int offset = (page - 1) * PAGESIZE;

	    int startPage = ((page - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
	    int endPage = startPage + BLOCK_SIZE - 1;
	    if (endPage > totalPages) {
	        endPage = totalPages;
	    }
		
	    List<PostSummary> postList = postService.getPagedPostsSummary(offset, PAGESIZE);

	    model.addAttribute("postList", postList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);

	    return "post/list";
	}
	
	/**
	 * 게시글 작성 페이지
	 */
	@GetMapping("/post/new")
	public String newPostForm() {
		
		return "post/write";
	}
	
	/**
	 * 게시글 작성 처리
	 * @return
	 */
	@PostMapping("/post")
	public String addPost(@ModelAttribute AddPostRequest request, 
			Principal principal, RedirectAttributes rttr) {
	
		Long authorId = memberService.findByUserId(principal.getName()).getId();
		
		postService.createPost(request, authorId);
		
		rttr.addFlashAttribute("message", "게시글이 성공적으로 등록되었습니다.");
		
		return "redirect:/";
	}
	
	/**
	 * 게시글 내용 가져오기
	 * @return
	 */
	@GetMapping("/post/{postId}")
	public String getPost(@PathVariable("postId") Long postId, Model model) {
		
		PostResponse post = postService.getPost(postId);
		List<CommentAndRepliesResponse> commentsAndReplies = postService.getCommentsAndReplies(postId);
		
		model.addAttribute("post", post);
		model.addAttribute("commentsAndReplies", commentsAndReplies);
		
		return "post/detail";
	}
	
	/**
	 * 댓글 작성하기
	 * @param parentId
	 * @return
	 */
	@PostMapping("/comment")
	public String addComment(@ModelAttribute AddCommentRequest request,
			Principal principal, RedirectAttributes rttr) {
		
		Long authorId = memberService.findByUserId(principal.getName()).getId();

		Long postId = postService.createComment(request, authorId);
		
		rttr.addFlashAttribute("message", "댓글이 성공적으로 등록되었습니다.");
		
		return "redirect:/post/" + postId;
	}
}