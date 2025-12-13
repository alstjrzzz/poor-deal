package com.khao.PoorDeal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khao.PoorDeal.domain.Comment;
import com.khao.PoorDeal.domain.CommentStatus;
import com.khao.PoorDeal.domain.ParentType;
import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.domain.PostType;
import com.khao.PoorDeal.dto.AddCommentRequest;
import com.khao.PoorDeal.dto.AddPostRequest;
import com.khao.PoorDeal.dto.CommentAndRepliesResponse;
import com.khao.PoorDeal.dto.CommentResponse;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSearchCondition;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.repository.CommentRepository;
import com.khao.PoorDeal.repository.PostRepository;

import lombok.RequiredArgsConstructor;

/**
 * @file PostService.java
 * @brief 게시물 및 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    
    /**
     * @brief 검색 조건에 맞는 게시물의 총 개수를 조회합니다.
     * @param condition 검색 조건(타입, 키워드)
     * @return 게시물 개수
     */
    public int getPostCount(PostSearchCondition condition) {
        return postRepository.selectPostCount(condition);
    }
    
    /**
     * @brief 게시물 목록을 페이징하여 요약 정보로 조회합니다.
     * @param offset 페이지 오프셋
     * @param pagesize 페이지당 항목 수
     * @param condition 검색 조건
     * @return 게시물 요약 DTO 리스트
     */
    public List<PostSummary> getPagedPostsSummary(int offset, int pagesize, PostSearchCondition condition) {
        return postRepository.selectPagedPosts(offset, pagesize, condition);
    }
    
    /**
     * @brief 특정 게시물의 상세 정보를 조회합니다.
     * @param id 게시물 ID
     * @return 게시물 상세 정보 DTO
     * @throws NoSuchElementException 해당 ID의 게시물이 없을 경우 발생
     */
    public PostResponse getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 게시글이 존재하지 않습니다: " + id));
    }
    
    /**
     * @brief 특정 게시물의 모든 댓글과 대댓글을 함께 조회합니다.
     * @param postId 게시물 ID
     * @return 댓글과 대댓글 목록을 포함하는 DTO 리스트
     */
    public List<CommentAndRepliesResponse> getCommentsAndReplies(Long postId) {
        List<CommentResponse> comments = commentRepository.findByParentIdAndParentType(postId, ParentType.POST);
        List<Long> commentIdList = comments.stream().map(CommentResponse::getId).collect(Collectors.toList());
        List<CommentResponse> replies = new ArrayList<>();
        if (!commentIdList.isEmpty()) {
            replies = commentRepository.findByParentIdListInAndParentType(commentIdList, ParentType.COMMENT);
        }
        
        if (replies == null) {
            replies = new ArrayList<>();
        }
        
        Map<Long, List<CommentResponse>> repliesMap = replies.stream()
                .collect(Collectors.groupingBy(CommentResponse::getParentId));
        
        return comments.stream()
                .map(comment -> {
                    List<CommentResponse> repliesForCurrentComment = repliesMap.getOrDefault(
                        comment.getId(), new ArrayList<>()
                    );
                    return CommentAndRepliesResponse.builder()
                            .comment(comment)
                            .replies(repliesForCurrentComment)
                            .build();
                }).collect(Collectors.toList());
    }
    
    /**
     * @brief 새로운 게시물을 생성합니다.
     * @details 게시물 타입에 따라 가격, 모집인원 등의 유효성을 검사하고, 이미지 파일을 S3에 업로드합니다.
     * @param request 게시물 생성 요청 DTO
     * @param authorId 작성자 ID
     */
    @Transactional
    public void createPost(AddPostRequest request, Long authorId) {
        
        PostType postType;
        try {
            postType = PostType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            postType = PostType.FREE;
        }

        Long price = 0L;
        Integer hiringQuota = 0;

        if (postType == PostType.TRADE) {
            if (request.getPrice() == null || request.getPrice() < 0) {
                throw new IllegalArgumentException("거래 금액은 0원 이상이어야 합니다.");
            }
            price = request.getPrice();
        } else if (postType == PostType.JOB) {
            if (request.getHiringQuota() == null || request.getHiringQuota() <= 0) {
                throw new IllegalArgumentException("모집 인원은 1명 이상이어야 합니다.");
            }
            hiringQuota = request.getHiringQuota();
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(authorId)
                .type(postType)
                .isAvailable(true)
                .price(price)
                .hiringQuota(hiringQuota)
                .filledCount(0)
                .build();
        
        postRepository.insert(post);
        
        Long postId = post.getId();
        String path = "post/";
        String imageUrl = s3Service.uploadFile(request.getImage(), path, postId);
        post.setImage(imageUrl);
        
        postRepository.update(post);
    }
    
    /**
     * @brief 새로운 댓글 또는 대댓글을 생성합니다.
     * @param request 댓글 생성 요청 DTO
     * @param authorId 작성자 ID
     * @return 댓글이 달린 원본 게시물의 ID
     * @throws IllegalArgumentException 부모 타입이 유효하지 않거나, 대댓글에 다시 댓글을 다는 경우 발생
     */
    @Transactional
    public Long createComment(AddCommentRequest request, Long authorId) {
        ParentType parentType;
        try {
            parentType = ParentType.valueOf(request.getParentType().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 부모 타입입니다.");
        }
        
        if (parentType == ParentType.COMMENT) {
            CommentResponse parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new NoSuchElementException("부모 댓글을 찾을 수 없습니다."));

            if (parentComment.getParentType() != ParentType.POST) {
                throw new IllegalArgumentException("대댓글에는 댓글을 달 수 없습니다.");
            }
        }
        
        Comment comment = Comment.builder()
            .parentType(parentType)
            .parentId(request.getParentId())
            .authorId(authorId)
            .content(request.getContent())
            .status(CommentStatus.ACTIVE)
            .build();
            
        commentRepository.save(comment);

        if (parentType == ParentType.POST) {
            return request.getParentId();
        } else {
            CommentResponse parentComment = commentRepository.findById(request.getParentId()).get();
            return parentComment.getParentId();
        }
    }
    
    /**
     * @brief 댓글을 수정합니다.
     * @param commentId 수정할 댓글 ID
     * @param newContent 새로운 내용
     * @param userId 요청한 사용자 ID
     * @throws NoSuchElementException 댓글이 존재하지 않을 경우 발생
     * @throws IllegalArgumentException 작성자가 아닌 경우 발생
     */
    @Transactional
    public void updateComment(Long commentId, String newContent, Long userId) {
        CommentResponse existing = commentRepository.findById(commentId)
            .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));
        
        if (!existing.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Comment comment = Comment.builder()
                .id(existing.getId())
                .content(newContent)
                .status(CommentStatus.MODIFIED)
                .build();
        
        commentRepository.update(comment);
    }

    /**
     * @brief 댓글을 삭제합니다. (논리적 삭제)
     * @param commentId 삭제할 댓글 ID
     * @param userId 요청한 사용자 ID
     * @throws NoSuchElementException 댓글이 존재하지 않을 경우 발생
     * @throws IllegalArgumentException 작성자가 아닌 경우 발생
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        CommentResponse existing = commentRepository.findById(commentId)
            .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        if (!existing.getAuthorId().equals(userId)) {
             throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        Comment comment = Comment.builder()
                .id(existing.getId())
                .content("삭제된 댓글입니다.")
                .status(CommentStatus.DELETED)
                .build();

        commentRepository.update(comment);
    }
    
    /**
     * @brief 게시물을 수정합니다.
     * @param postId 수정할 게시물 ID
     * @param request 게시물 수정 요청 DTO
     * @param userId 요청한 사용자 ID
     * @throws NoSuchElementException 게시물이 존재하지 않을 경우 발생
     * @throws IllegalArgumentException 작성자가 아닌 경우 발생
     */
    @Transactional
    public void updatePost(Long postId, AddPostRequest request, Long userId) {
        
        PostResponse existing = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (!existing.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        PostType postType = existing.getType(); 

        String imageUrl = existing.getImage();
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            s3Service.deleteFile("post/", postId, existing.getImage());
            imageUrl = s3Service.uploadFile(request.getImage(), "post/", postId);
        }
        
        Long price = 0L;
        Integer hiringQuota = 0;

        if (postType == PostType.TRADE) {
            if (request.getPrice() != null) {
                if (request.getPrice() < 0) throw new IllegalArgumentException("거래 금액은 0원 이상이어야 합니다.");
                price = request.getPrice();
            } else {
                price = existing.getPrice();
            }
        } else if (postType == PostType.JOB) {
            if (request.getHiringQuota() != null) {
                if (request.getHiringQuota() <= 0) throw new IllegalArgumentException("모집 인원은 1명 이상이어야 합니다.");
                hiringQuota = request.getHiringQuota();
            } else {
                hiringQuota = existing.getHiringQuota();
            }
        }

        Post post = Post.builder()
                .id(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(userId)
                .type(postType)
                .price(price)
                .hiringQuota(hiringQuota)
                .filledCount(existing.getFilledCount())
                .isAvailable(existing.isAvailable()) 
                .image(imageUrl)
                .build();

        postRepository.update(post);
    }

    /**
     * @brief 게시물을 삭제합니다. (논리적 삭제)
     * @param postId 삭제할 게시물 ID
     * @param userId 요청한 사용자 ID
     * @throws NoSuchElementException 게시물이 존재하지 않을 경우 발생
     * @throws IllegalArgumentException 작성자가 아닌 경우 발생
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        PostResponse existing = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (!existing.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(postId);
    }
}
