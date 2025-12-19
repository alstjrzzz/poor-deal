package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Post;
import com.khao.PoorDeal.dto.PostResponse;
import com.khao.PoorDeal.dto.PostSearchCondition;
import com.khao.PoorDeal.dto.PostSummary;

/**
 * @file PostMapper.java
 * @brief 게시물 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Mapper
public interface PostMapper {

	/**
	 * @brief 검색 조건에 맞는 게시물의 총 개수를 조회합니다.
	 * @param condition 검색 조건 DTO
	 * @return 게시물 개수
	 */
	public int selectPostCount(@Param("condition") PostSearchCondition condition);

	/**
	 * @brief 기존 게시물을 수정합니다.
	 * @param post 수정할 게시물 객체
	 */
	public void update(Post post);

	/**
	 * @brief ID로 게시물을 삭제 처리합니다. (isDeleted = true)
	 * @param id 게시물 ID
	 */
	public void delete(Long id);

	/**
	 * @brief 검색 조건에 맞는 게시물 목록을 페이징하여 조회합니다.
	 * @param offset 페이지 오프셋
	 * @param limit 페이지당 항목 수
	 * @param condition 검색 조건 DTO
	 * @return 게시물 요약 DTO 리스트
	 */
	public List<PostSummary> selectPagedPosts(
            @Param("offset") int offset, 
            @Param("limit") int limit,
            @Param("condition") PostSearchCondition condition
    );

	/**
	 * @brief 새로운 게시물을 데이터베이스에 삽입합니다.
	 * @param post 삽입할 게시물 객체
	 */
	public void insert(Post post);

	/**
	 * @brief ID로 게시물을 찾아 응답 DTO로 반환합니다.
	 * @param id 게시물 ID
	 * @return PostResponse DTO를 포함하는 Optional 객체
	 */
	public Optional<PostResponse> findById(Long id);
}
