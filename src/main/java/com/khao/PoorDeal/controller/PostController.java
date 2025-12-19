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
import com.khao.PoorDeal.dto.PostSearchCondition;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.service.MailService;
import com.khao.PoorDeal.service.MemberService;
import com.khao.PoorDeal.service.PostService;

import lombok.RequiredArgsConstructor;

/**
 * @file PostController.java
 * @brief 게시물 및 댓글 관련 CRUD 웹 요청을 처리하는 컨트롤러 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Controller
@RequiredArgsConstructor
public class PostController {

    @Value("${post.page.size:10}")
    private int PAGESIZE = 10;
    
    @Value("${post.page.block_size:10}")
    private int BLOCK_SIZE = 10;
    
    private final PostService postService;
    private final MemberService memberService;
    private final MailService mailService;
    
    /**
     * @brief 메인 페이지. 게시물 목록을 페이징하여 조회합니다.
     * @param page 요청 페이지 번호
     * @param condition 검색 조건 (타입, 키워드)
     * @param model 뷰에 전달할 모델
     * @param principal 현재 로그인한 사용자 정보
     * @return "post/list" 뷰
     */
    @GetMapping("/")
    public String getPosts(
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute("searchCondition") PostSearchCondition condition,
            Model model, Principal principal) {
        
        int totalCount = postService.getPostCount(condition);
        int totalPages = (int) Math.ceil((double) totalCount / PAGESIZE);
        if (totalPages == 0) totalPages = 1;

        int offset = (page - 1) * PAGESIZE;
        int startPage = ((page - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
        int endPage = startPage + BLOCK_SIZE - 1;
        if (endPage > totalPages) {
            endPage = totalPages;
        }
        
        List<PostSummary> postList = postService.getPagedPostsSummary(offset, PAGESIZE, condition);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        if (principal != null) {
            Long point = memberService.findByUserId(principal.getName()).getPoint();
            model.addAttribute("myPoint", point);
        }

        return "post/list";
    }
    
    /**
     * @brief 새 게시물 작성 폼 페이지를 반환합니다.
     * @return "post/write" 뷰
     */
    @GetMapping("/post/new")
    public String newPostForm() {
        return "post/write";
    }

    /**
     * @brief 새 게시물 작성을 처리합니다.
     * @param request 게시물 추가 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 성공 시 메인 페이지("/")로, 실패 시 작성 폼으로 리다이렉트
     */
    @PostMapping("/post")
    public String addPost(@ModelAttribute AddPostRequest request, 
            Principal principal, RedirectAttributes rttr) {
    
        Long authorId = memberService.findByUserId(principal.getName()).getId();
        
        try {
            postService.createPost(request, authorId);
            rttr.addFlashAttribute("message", "게시글이 성공적으로 등록되었습니다.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            rttr.addFlashAttribute("error", "등록 실패: " + e.getMessage());
            return "redirect:/post/new";
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "오류 발생: " + e.getMessage());
            return "redirect:/";
        }
    }
    
    /**
     * @brief 특정 게시물의 상세 페이지를 조회합니다.
     * @param postId 조회할 게시물 ID
     * @param condition 현재 검색 조건 (페이지 이동 시 유지)
     * @param page 현재 페이지 번호 (페이지 이동 시 유지)
     * @param model 뷰에 전달할 모델
     * @param principal 현재 로그인한 사용자 정보
     * @return "post/detail" 뷰
     */
    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable("postId") Long postId,
                        @ModelAttribute("searchCondition") PostSearchCondition condition, 
                        @RequestParam(defaultValue = "1") int page,
                        Model model, Principal principal) {
        
        PostResponse post = postService.getPost(postId);
        List<CommentAndRepliesResponse> commentsAndReplies = postService.getCommentsAndReplies(postId);
        int totalCommentCount = commentsAndReplies.stream()
                .mapToInt(cr -> 1 + (cr.getReplies() != null ? cr.getReplies().size() : 0))
                .sum();
        
        boolean isApplied = false;
        Long loginId = memberService.findByUserId(principal.getName()).getId();
        if (!loginId.equals(post.getAuthorId())) {
            isApplied = mailService.hasApplied(loginId, postId);
        }
        
        model.addAttribute("post", post);
        model.addAttribute("commentsAndReplies", commentsAndReplies);
        model.addAttribute("totalCommentCount", totalCommentCount);
        model.addAttribute("isApplied", isApplied);
        model.addAttribute("loginId", loginId);
        model.addAttribute("currentPage", page);
        
        return "post/detail";
    }

    /**
     * @brief 새 댓글 작성을 처리합니다.
     * @param request 댓글 추가 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 해당 게시물의 상세 페이지로 리다이렉트
     */
    @PostMapping("/comment")
    public String addComment(@ModelAttribute AddCommentRequest request,
            Principal principal, RedirectAttributes rttr) {
        
        Long authorId = memberService.findByUserId(principal.getName()).getId();
        Long postId = postService.createComment(request, authorId);
        rttr.addFlashAttribute("message", "댓글이 성공적으로 등록되었습니다.");
        
        return "redirect:/post/" + postId;
    }

    /**
     * @brief 댓글 수정을 처리합니다.
     * @param commentId 수정할 댓글 ID
     * @param postId 댓글이 속한 게시물 ID
     * @param content 새로운 댓글 내용
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 해당 게시물의 상세 페이지로 리다이렉트
     */
    @PostMapping("/comment/update")
    public String updateComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("postId") Long postId,
                                @RequestParam("content") String content,
                                Principal principal, RedirectAttributes rttr) {
        
        Long userId = memberService.findByUserId(principal.getName()).getId();
        
        try {
            postService.updateComment(commentId, content, userId);
            rttr.addFlashAttribute("message", "댓글이 수정되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/post/" + postId;
    }

    /**
     * @brief 댓글 삭제를 처리합니다.
     * @param commentId 삭제할 댓글 ID
     * @param postId 댓글이 속한 게시물 ID
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 해당 게시물의 상세 페이지로 리다이렉트
     */
    @PostMapping("/comment/delete")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("postId") Long postId,
                                Principal principal, RedirectAttributes rttr) {
        
        Long userId = memberService.findByUserId(principal.getName()).getId();

        try {
            postService.deleteComment(commentId, userId);
            rttr.addFlashAttribute("message", "댓글이 삭제되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/post/" + postId;
    }

    /**
     * @brief 게시물 수정 폼 페이지를 반환합니다.
     * @param postId 수정할 게시물 ID
     * @param model 뷰에 전달할 모델
     * @param principal 현재 로그인한 사용자 정보
     * @return "post/edit" 뷰. 작성자가 아니면 상세 페이지로 리다이렉트.
     */
    @GetMapping("/post/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, Model model, Principal principal) {
        PostResponse post = postService.getPost(postId);
        Long loginId = memberService.findByUserId(principal.getName()).getId();

        if (!post.getAuthorId().equals(loginId)) {
            return "redirect:/post/" + postId;
        }

        model.addAttribute("post", post);
        return "post/edit";
    }

    /**
     * @brief 게시물 수정을 처리합니다.
     * @param postId 수정할 게시물 ID
     * @param request 게시물 수정 요청 DTO
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 성공 시 상세 페이지로, 실패 시 수정 폼으로 리다이렉트
     */
    @PostMapping("/post/{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId,
                             @ModelAttribute AddPostRequest request,
                             Principal principal, RedirectAttributes rttr) {
        
        Long userId = memberService.findByUserId(principal.getName()).getId();

        try {
            postService.updatePost(postId, request, userId);
            rttr.addFlashAttribute("message", "게시글이 수정되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "수정 실패: " + e.getMessage());
            return "redirect:/post/" + postId + "/edit";
        }

        return "redirect:/post/" + postId;
    }

    /**
     * @brief 게시물 삭제를 처리합니다.
     * @param postId 삭제할 게시물 ID
     * @param principal 현재 로그인한 사용자 정보
     * @param rttr 리다이렉트 시 전달할 속성
     * @return 성공 시 메인 페이지로, 실패 시 상세 페이지로 리다이렉트
     */
    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId,
                             Principal principal, RedirectAttributes rttr) {
        
        Long userId = memberService.findByUserId(principal.getName()).getId();

        try {
            postService.deletePost(postId, userId);
            rttr.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "삭제 실패: " + e.getMessage());
            return "redirect:/post/" + postId;
        }

        return "redirect:/";
    }
}
