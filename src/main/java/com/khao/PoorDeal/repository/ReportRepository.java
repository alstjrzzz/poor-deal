package com.khao.PoorDeal.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;
import com.khao.PoorDeal.dto.ReportResponse;
import com.khao.PoorDeal.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

    private final ReportMapper reportMapper;

    public void save(Report report) {
        reportMapper.save(report);
    }

    public List<ReportResponse> findAllResponses() {
        return reportMapper.findAllResponses();
    }

    public Report findById(Long id) {
        return reportMapper.findById(id);
    }

    public void updateStatus(Long id, ReportStatus status) {
        reportMapper.updateStatus(id, status);
    }
}
