package com.khao.PoorDeal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Comment;
import com.khao.PoorDeal.domain.ParentType;
import com.khao.PoorDeal.dto.CommentResponse;
import com.khao.PoorDeal.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

/**
 * @file CommentRepository.java
 * @brief 댓글 데이터 처리를 위한 레포지토리 클래스입니다. CommentMapper를 사용하여 데이터베이스와 연동합니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Repository
@RequiredArgsConstructor
public class CommentRepository {

	private final CommentMapper commentMapper;
	
	/** @see com.khao.PoorDeal.mapper.CommentMapper#save(Comment) */
	public void save(Comment comment) { commentMapper.save(comment); }
	/** @see com.khao.PoorDeal.mapper.CommentMapper#update(Comment) */
	public void update(Comment comment) { commentMapper.update(comment); }
	/** @see com.khao.PoorDeal.mapper.CommentMapper#findById(Long) */
	public Optional<CommentResponse> findById(Long id) { return commentMapper.findById(id); }
	/** @see com.khao.PoorDeal.mapper.CommentMapper#findByParentIdAndParentType(Long, ParentType) */
	public List<CommentResponse> findByParentIdAndParentType(Long parentId, ParentType parentType) {
		return commentMapper.findByParentIdAndParentType(parentId, parentType); }
	/** @see com.khao.PoorDeal.mapper.CommentMapper#findByParentIdListInAndParentType(List, ParentType) */
	public List<CommentResponse> findByParentIdListInAndParentType(List<Long> parentIds, ParentType parentType) {
		return commentMapper.findByParentIdListInAndParentType(parentIds, parentType); }
	
}
