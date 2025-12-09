package com.khao.PoorDeal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.khao.PoorDeal.domain.Report;
import com.khao.PoorDeal.domain.ReportStatus;

@Mapper
public interface ReportMapper {

    public void save(Report report);
    public List<Report> findAll();
    public List<Report> findByReporterId(Long reporterId);
    public Report findById(Long id);
    public void updateStatus(@Param("id") Long id, @Param("status") ReportStatus status);
}
