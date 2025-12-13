package com.khao.PoorDeal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;
import com.khao.PoorDeal.dto.ReportRequest;
import com.khao.PoorDeal.dto.ReportResponse;
import com.khao.PoorDeal.repository.MemberRepository;
import com.khao.PoorDeal.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

/**
 * @file ReportService.java
 * @brief 사용자 신고 접수 및 처리 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 * @author gnfle
 * @date 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    /**
     * @brief 새로운 사용자 신고를 접수합니다.
     * @param request 신고 내용을 담은 DTO
     * @throws IllegalArgumentException 자기 자신을 신고하는 경우 발생
     */
    @Transactional
    public void submitReport(ReportRequest request) {
    	
        if (request.getReporterId().equals(request.getSuspectId())) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }

        Report report = Report.builder()
                .reason(request.getReason())
                .reporterId(request.getReporterId())
                .postId(request.getPostId())
                .suspectId(request.getSuspectId())
                .status(ReportStatus.PENDING)
                .build();

        reportRepository.save(report);
    }

    /**
     * @brief 관리자가 모든 신고 목록을 조회합니다.
     * @return 모든 신고 정보 DTO 리스트
     */
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllResponses();
    }

    /**
     * @brief 관리자가 신고를 처리합니다 (승인 또는 반려).
     * @details 신고를 승인(`APPROVED`)하면 피신고자의 계정이 차단됩니다.
     * @param reportId 처리할 신고의 ID
     * @param newStatus 새로운 처리 상태 (APPROVED, REJECTED 등)
     */
    @Transactional
    public void processReport(Long reportId, ReportStatus newStatus) {
        
        reportRepository.updateStatus(reportId, newStatus);

        if (newStatus == ReportStatus.APPROVED) {
            Report report = reportRepository.findById(reportId);
            memberRepository.updateBlockStatus(report.getSuspectId(), true);
        }
    }
}
