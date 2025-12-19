package com.khao.PoorDeal.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;
import com.khao.PoorDeal.dto.ReportResponse;
import com.khao.PoorDeal.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;

/**
 * @file ReportRepository.java
 * @brief 신고 데이터 처리를 위한 레포지토리 클래스입니다. ReportMapper를 사용하여 데이터베이스와 연동합니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Repository
@RequiredArgsConstructor
public class ReportRepository {

    private final ReportMapper reportMapper;

    /** @see com.khao.PoorDeal.mapper.ReportMapper#save(Report) */
    public void save(Report report) {
        reportMapper.save(report);
    }

    /** @see com.khao.PoorDeal.mapper.ReportMapper#findAllResponses() */
    public List<ReportResponse> findAllResponses() {
        return reportMapper.findAllResponses();
    }

    /** @see com.khao.PoorDeal.mapper.ReportMapper#findById(Long) */
    public Report findById(Long id) {
        return reportMapper.findById(id);
    }

    /** @see com.khao.PoorDeal.mapper.ReportMapper#updateStatus(Long, ReportStatus) */
    public void updateStatus(Long id, ReportStatus status) {
        reportMapper.updateStatus(id, status);
    }
}
