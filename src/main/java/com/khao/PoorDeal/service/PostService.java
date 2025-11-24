package com.khao.PoorDeal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khao.PoorDeal.domain.Comment;
import com.khao.PoorDeal.domain.ParentType;
import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.domain.PostType;
import com.khao.PoorDeal.dto.AddCommentRequest;
import com.khao.PoorDeal.dto.AddPostRequest;
import com.khao.PoorDeal.dto.CommentAndRepliesResponse;
import com.khao.PoorDeal.dto.CommentResponse;
import com.khao.PoorDeal.dto.PostResponse;
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
	
	
	public int getPostCount() {
		
		return postRepository.selectPostCount();
	}
	
	public List<PostSummary> getPagedPostsSummary(int offset, int pagesize) {
		
		return postRepository.selectPagedPosts(offset, pagesize);
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

		Post post = Post.builder()
				.title(request.getTitle())
				.content(request.getContent())
				.authorId(authorId)
				.type(postType)
				.isAvailable(true)
				.price(request.getPrice())
				.hiringQuota(request.getHiringQuota())
				.filledCount(0)
				.build();
		
		postRepository.insert(post);
		
		Long postId = post.getId();
		String path = "post/";
        String imageUrl = s3Service.uploadFile(request.getImage(), path, postId);
        post.setImage(imageUrl);
        
        postRepository.update(post);
	}
	
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
            .build();
            
        commentRepository.save(comment);

        if (parentType == ParentType.POST) {
            return request.getParentId();
        } else {
        	CommentResponse parentComment = commentRepository.findById(request.getParentId()).get();
            return parentComment.getParentId();
        }
	}
}
