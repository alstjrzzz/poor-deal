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

@Repository
@RequiredArgsConstructor
public class MailRepository {

	private final MailMapper mailMapper;
	
	public int selectMailCountByMemberId(Long memberId) {
		return mailMapper.selectMailCountByMemberId(memberId); }
	public boolean existsBySenderIdAndPostId(Long senderId, Long postId) {
		return mailMapper.existsBySenderIdAndPostId(senderId, postId); }
	public List<MailSummary> selectPagedMailsByMemberId(int offset, int pagesize, Long memberId) {
		return mailMapper.selectPagedMailsByMemberId(offset, pagesize, memberId); }
	public void save(Mail mail) { mailMapper.save(mail); }
	public Optional<Mail> findById(Long id) { return mailMapper.findById(id); }
	public Optional<MailResponse> findMailResponseById(Long id) { return mailMapper.findMailResponseById(id); }
	public void updateMailStatus(Mail mail) { mailMapper.updateMailStatus(mail); }
}
