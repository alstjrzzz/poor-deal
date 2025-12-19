package com.khao.PoorDeal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSearchCondition;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

/**
 * @file PostRepository.java
 * @brief 게시물 데이터 처리를 위한 레포지토리 클래스입니다. PostMapper를 사용하여 데이터베이스와 연동합니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Repository
@RequiredArgsConstructor
public class PostRepository {

	private final PostMapper postMapper;
	
	/** @see com.khao.PoorDeal.mapper.PostMapper#selectPostCount(PostSearchCondition) */
	public int selectPostCount(PostSearchCondition condition) { 
        return postMapper.selectPostCount(condition); 
    }
	/** @see com.khao.PoorDeal.mapper.PostMapper#update(Post) */
	public void update(Post post) { postMapper.update(post); }
	/** @see com.khao.PoorDeal.mapper.PostMapper#delete(Long) */
	public void delete(Long id) { postMapper.delete(id); }
	/** @see com.khao.PoorDeal.mapper.PostMapper#selectPagedPosts(int, int, PostSearchCondition) */
	public List<PostSummary> selectPagedPosts(int offset, int pagesize, PostSearchCondition condition) { 
        return postMapper.selectPagedPosts(offset, pagesize, condition); 
    }
	/** @see com.khao.PoorDeal.mapper.PostMapper#insert(Post) */
	public void insert(Post post) { postMapper.insert(post); }
	/** @see com.khao.PoorDeal.mapper.PostMapper#findById(Long) */
	public Optional<PostResponse> findById(Long id) { return postMapper.findById(id); }
}
