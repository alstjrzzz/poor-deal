package com.khao.PoorDeal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;
import com.khao.PoorDeal.dto.ReportResponse;

@Mapper
public interface ReportMapper {
	
    void save(Report report);
    List<ReportResponse> findAllResponses();
    Report findById(Long id);
    void updateStatus(@Param("id") Long id, @Param("status") ReportStatus status);
}
