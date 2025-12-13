package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostSummary;

/**
 * @file MailMapper.java
 * @brief 쪽지 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Mapper
public interface MailMapper {

	/**
	 * @brief 특정 회원이 받은 쪽지의 총 개수를 조회합니다.
	 * @param memberId 회원 ID
	 * @return 쪽지 개수
	 */
	public int selectMailCountByMemberId(Long memberId);

	/**
	 * @brief 특정 발신자가 특정 게시물에 대해 보낸 쪽지가 있는지 확인합니다.
	 * @param senderId 발신자 ID
	 * @param postId 게시물 ID
	 * @return 존재하면 true, 그렇지 않으면 false
	 */
	public boolean existsBySenderIdAndPostId(
			@Param("senderId") Long senderId, @Param("postId") Long postId);

	/**
	 * @brief 특정 회원의 쪽지 목록을 페이징하여 조회합니다.
	 * @param offset 페이지 오프셋
	 * @param limit 페이지당 항목 수
	 * @param memberId 회원 ID
	 * @return 쪽지 요약 DTO 리스트
	 */
	public List<MailSummary> selectPagedMailsByMemberId(
			@Param("offset") int offset, @Param("limit") int limit, @Param("memberId") Long memberId);

	/**
	 * @brief 새로운 쪽지를 데이터베이스에 저장합니다.
	 * @param mail 저장할 쪽지 객체
	 */
	public void save(Mail mail);

	/**
	 * @brief ID로 쪽지를 찾아 도메인 객체로 반환합니다.
	 * @param id 쪽지 ID
	 * @return Mail 도메인 객체를 포함하는 Optional 객체
	 */
	public Optional<Mail> findById(Long id);

	/**
	 * @brief ID로 쪽지를 찾아 응답 DTO로 반환합니다.
	 * @param id 쪽지 ID
	 * @return MailResponse DTO를 포함하는 Optional 객체
	 */
	public Optional<MailResponse> findMailResponseById(Long id);

	/**
	 * @brief 쪽지의 상태를 업데이트합니다.
	 * @param mail 상태를 업데이트할 쪽지 객체
	 */
	public void updateMailStatus(Mail mail);
}
