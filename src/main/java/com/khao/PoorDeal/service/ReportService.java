package com.khao.PoorDeal.service;

import org.springframework.stereotype.Service;

import com.khao.PoorDeal.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final ReportRepository reportRepository;
	
	/*
	 @Transactional
    public void submitReport(Report report) {
        // 초기 상태 설정
        report.setStatus(ReportStatus.PENDING);
        
        // 유효성 검사 (예: 자기 자신 신고 불가 등)
        if (report.getReporterId().equals(report.getSuspectId())) {
             throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }
        
        reportMapper.save(report);
    }

    public List<Report> getAllReports() {
        return reportMapper.findAll();
    }

    @Transactional
    public void processReport(Long reportId, ReportStatus newStatus) {
        // 1. 상태 업데이트
        reportMapper.updateStatus(reportId, newStatus);
        
        // 2. 만약 승인(APPROVED)이라면, 해당 유저(Suspect)에게 제재 가하기
        if (newStatus == ReportStatus.APPROVED) {
            Report report = reportMapper.findById(reportId);
            // memberService.banUser(report.getSuspectId()); // 예시 로직
        }
    }
	 */
}
