package com.khao.PoorDeal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepository {

	private final PostMapper postMapper;
	
	public int selectPostCount() { return postMapper.selectPostCount(); }
	public void update(Post post) { postMapper.update(post); }
	public List<PostSummary> selectPagedPosts(int offset, int pagesize) { return postMapper.selectPagedPosts(offset, pagesize); }
	public void insert(Post post) { postMapper.insert(post); }
	public Optional<PostResponse> findById(Long id) { return postMapper.findById(id); }
	
}
