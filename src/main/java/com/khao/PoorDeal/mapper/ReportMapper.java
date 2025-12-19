package com.khao.PoorDeal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;
import com.khao.PoorDeal.dto.ReportResponse;

/**
 * @file ReportMapper.java
 * @brief 신고 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Mapper
public interface ReportMapper {
	
	/**
	 * @brief 새로운 신고를 데이터베이스에 저장합니다.
	 * @param report 저장할 신고 객체
	 */
    void save(Report report);

    /**
     * @brief 모든 신고 내역을 응답 DTO 리스트로 조회합니다.
     * @return ReportResponse DTO 리스트
     */
    List<ReportResponse> findAllResponses();

    /**
     * @brief ID로 신고를 찾아 도메인 객체로 반환합니다.
     * @param id 신고 ID
     * @return Report 도메인 객체
     */
    Report findById(Long id);

    /**
     * @brief 특정 신고의 처리 상태를 업데이트합니다.
     * @param id 신고 ID
     * @param status 변경할 상태
     */
    void updateStatus(@Param("id") Long id, @Param("status") ReportStatus status);
}
