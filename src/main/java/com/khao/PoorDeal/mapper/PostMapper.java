package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSummary;

@Mapper
public interface PostMapper {

	public int selectPostCount();
	public void update(Post post);
	public List<PostSummary> selectPagedPosts(@Param("offset") int offset, @Param("limit") int limit);
	public void insert(Post post);
	public Optional<PostResponse> findById(Long id);
}
