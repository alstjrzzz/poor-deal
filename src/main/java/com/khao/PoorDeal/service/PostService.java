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

@Service
@RequiredArgsConstructor
public class PostService {

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    
    public int getPostCount(PostSearchCondition condition) {
        return postRepository.selectPostCount(condition);
    }
    
    public List<PostSummary> getPagedPostsSummary(int offset, int pagesize, PostSearchCondition condition) {
        return postRepository.selectPagedPosts(offset, pagesize, condition);
    }
    
    public PostResponse getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 게시글이 존재하지 않습니다: " + id));
    }
    
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
    
    @Transactional
    public void updatePost(Long postId, AddPostRequest request, Long userId) {
        
        PostResponse existing = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (!existing.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        // 1. 타입 결정 (수정 시 타입 변경 불가라면 existing.getType()을 써야 함. 
        // 여기선 request에서 오지만, 보통은 변경 안 되게 막거나 기존 값 유지)
        // 화면에서 disabled 처리를 했더라도 서버에서 한 번 더 existing 타입을 쓰는 게 안전함.
        PostType postType = existing.getType(); 

        // 2. 이미지 처리
        String imageUrl = existing.getImage();
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            s3Service.deleteFile("post/", postId, existing.getImage());
            imageUrl = s3Service.uploadFile(request.getImage(), "post/", postId);
        }
        
        // 3. [수정] 타입별 데이터 검증 및 업데이트
        Long price = 0L;
        Integer hiringQuota = 0;

        if (postType == PostType.TRADE) {
            // 가격이 null이면 기존 가격 유지, 아니면 새 값 검증
            if (request.getPrice() != null) {
                if (request.getPrice() < 0) throw new IllegalArgumentException("거래 금액은 0원 이상이어야 합니다.");
                price = request.getPrice();
            } else {
                price = existing.getPrice();
            }
        } else if (postType == PostType.JOB) {
            // 인원이 null이면 기존 인원 유지, 아니면 새 값 검증
            if (request.getHiringQuota() != null) {
                if (request.getHiringQuota() <= 0) throw new IllegalArgumentException("모집 인원은 1명 이상이어야 합니다.");
                hiringQuota = request.getHiringQuota();
            } else {
                hiringQuota = existing.getHiringQuota();
            }
        }
        // FREE는 0, 0 유지

        Post post = Post.builder()
                .id(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(userId)
                .type(postType) // 기존 타입 유지
                .price(price)
                .hiringQuota(hiringQuota)
                .filledCount(existing.getFilledCount())
                .isAvailable(existing.isAvailable()) 
                .image(imageUrl)
                .build();

        postRepository.update(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        PostResponse existing = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (!existing.getAuthorId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(postId);
        // s3Service.deleteFile("post/", postId, existing.getImage()); // soft delete or hard delete policy
    }
}