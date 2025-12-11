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

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    /**
     * 신고 접수
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
     * 관리자용 전체 신고 목록 조회
     */
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllResponses();
    }

    /**
     * 신고 처리 (승인/반려)
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
