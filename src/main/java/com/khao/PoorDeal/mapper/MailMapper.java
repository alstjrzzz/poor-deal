package com.khao.PoorDeal.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Mail;
import com.khao.PoorDeal.dto.MailResponse;
import com.khao.PoorDeal.dto.MailSummary;
import com.khao.PoorDeal.dto.PostSummary;

@Mapper
public interface MailMapper {

	public int selectMailCountByMemberId(Long memberId);
	public boolean existsBySenderIdAndPostId(
			@Param("senderId") Long senderId, @Param("postId") Long postId);
	public List<MailSummary> selectPagedMailsByMemberId(
			@Param("offset") int offset, @Param("limit") int limit, @Param("memberId") Long memberId);
	public void save(Mail mail);
	public Optional<Mail> findById(Long id);
	public Optional<MailResponse> findMailResponseById(Long id);
	public void updateMailStatus(Mail mail);
}
