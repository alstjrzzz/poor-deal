package com.khao.PoorDeal.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostSummary;
import com.khao.PoorDeal.mapper.MailMapper;

import lombok.RequiredArgsConstructor;

/**
 * @file MailRepository.java
 * @brief 쪽지 데이터 처리를 위한 레포지토리 클래스입니다. MailMapper를 사용하여 데이터베이스와 연동합니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Repository
@RequiredArgsConstructor
public class MailRepository {

	private final MailMapper mailMapper;
	
	/** @see com.khao.PoorDeal.mapper.MailMapper#selectMailCountByMemberId(Long) */
	public int selectMailCountByMemberId(Long memberId) {
		return mailMapper.selectMailCountByMemberId(memberId); }
	/** @see com.khao.PoorDeal.mapper.MailMapper#existsBySenderIdAndPostId(Long, Long) */
	public boolean existsBySenderIdAndPostId(Long senderId, Long postId) {
		return mailMapper.existsBySenderIdAndPostId(senderId, postId); }
	/** @see com.khao.PoorDeal.mapper.MailMapper#selectPagedMailsByMemberId(int, int, Long) */
	public List<MailSummary> selectPagedMailsByMemberId(int offset, int pagesize, Long memberId) {
		return mailMapper.selectPagedMailsByMemberId(offset, pagesize, memberId); }
	/** @see com.khao.PoorDeal.mapper.MailMapper#save(Mail) */
	public void save(Mail mail) { mailMapper.save(mail); }
	/** @see com.khao.PoorDeal.mapper.MailMapper#findById(Long) */
	public Optional<Mail> findById(Long id) { return mailMapper.findById(id); }
	/** @see com.khao.PoorDeal.mapper.MailMapper#findMailResponseById(Long) */
	public Optional<MailResponse> findMailResponseById(Long id) { return mailMapper.findMailResponseById(id); }
	/** @see com.khao.PoorDeal.mapper.MailMapper#updateMailStatus(Mail) */
	public void updateMailStatus(Mail mail) { mailMapper.updateMailStatus(mail); }
}
