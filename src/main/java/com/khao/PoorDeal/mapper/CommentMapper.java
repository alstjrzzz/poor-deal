package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Comment;
import com.khao.PoorDeal.domain.ParentType;
import com.khao.PoorDeal.dto.CommentResponse;

@Mapper
public interface CommentMapper {

	public void save(Comment comment);
	public Optional<CommentResponse> findById(Long id);
	public List<CommentResponse> findByParentIdAndParentType(
			@Param("parentId") Long parentId, 
			@Param("parentType") ParentType parentType);
	public List<CommentResponse> findByParentIdListInAndParentType(
			@Param("parentIds") List<Long> parentIds, 
			@Param("parentType") ParentType parentType);
}
