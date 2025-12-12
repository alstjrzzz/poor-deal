package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSearchCondition;
import com.khao.PoorDeal.dto.PostSummary;

@Mapper
public interface PostMapper {

	public int selectPostCount(@Param("condition") PostSearchCondition condition);	public void update(Post post);
	public void delete(Long id);
	public List<PostSummary> selectPagedPosts(
            @Param("offset") int offset, 
            @Param("limit") int limit,
            @Param("condition") PostSearchCondition condition
    );
	public void insert(Post post);
	public Optional<PostResponse> findById(Long id);
}
