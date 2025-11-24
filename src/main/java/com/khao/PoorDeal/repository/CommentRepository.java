package com.khao.PoorDeal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Comment;
import com.khao.PoorDeal.domain.ParentType;
import com.khao.PoorDeal.dto.CommentResponse;
import com.khao.PoorDeal.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

	private final CommentMapper commentMapper;
	
	public void save(Comment comment) { commentMapper.save(comment); }
	public Optional<CommentResponse> findById(Long id) { return commentMapper.findById(id); }
	public List<CommentResponse> findByParentIdAndParentType(Long parentId, ParentType parentType) {
		return commentMapper.findByParentIdAndParentType(parentId, parentType); }
	public List<CommentResponse> findByParentIdListInAndParentType(List<Long> parentIds, ParentType parentType) {
		return commentMapper.findByParentIdListInAndParentType(parentIds, parentType); }
	
}
