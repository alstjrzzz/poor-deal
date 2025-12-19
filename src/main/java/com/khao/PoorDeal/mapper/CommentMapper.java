package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Comment;
import com.khao.PoorDeal.domain.ParentType;
import com.khao.PoorDeal.dto.CommentResponse;

/**
 * @file CommentMapper.java
 * @brief 댓글 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Mapper
public interface CommentMapper {

	/**
	 * @brief 새로운 댓글을 데이터베이스에 저장합니다.
	 * @param comment 저장할 댓글 객체
	 */
	public void save(Comment comment);

	/**
	 * @brief 기존 댓글의 내용을 수정합니다.
	 * @param comment 수정할 댓글 객체
	 */
	void update(Comment comment);

	/**
	 * @brief ID로 댓글을 찾아 응답 DTO로 반환합니다.
	 * @param id 댓글 ID
	 * @return CommentResponse DTO를 포함하는 Optional 객체
	 */
	public Optional<CommentResponse> findById(Long id);

	/**
	 * @brief 특정 부모에 속한 모든 댓글을 찾아 반환합니다.
	 * @param parentId 부모 엔티티의 ID
	 * @param parentType 부모 엔티티의 타입
	 * @return 댓글 응답 DTO 리스트
	 */
	public List<CommentResponse> findByParentIdAndParentType(
			@Param("parentId") Long parentId, 
			@Param("parentType") ParentType parentType);

	/**
	 * @brief 여러 부모 ID 목록에 속한 모든 대댓글을 찾아 반환합니다.
	 * @param parentIds 부모 댓글 ID 리스트
	 * @param parentType 부모 엔티티의 타입 (주로 COMMENT)
	 * @return 대댓글 응답 DTO 리스트
	 */
	public List<CommentResponse> findByParentIdListInAndParentType(
			@Param("parentIds") List<Long> parentIds, 
			@Param("parentType") ParentType parentType);
}
