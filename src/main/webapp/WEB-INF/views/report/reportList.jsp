<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>관리자 - 신고 관리</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f1f5f9; }
        .admin-container { padding: 30px; }
        .admin-card { border: none; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); background: white; overflow: hidden; }
        .navbar-admin { background: #212529; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .table thead th { background-color: #f8f9fa; border-bottom: 2px solid #dee2e6; font-weight: 600; color: #495057; text-align: center; vertical-align: middle; white-space: nowrap; }
        .table tbody td { vertical-align: middle; font-size: 0.95rem; }
        .reporter-info { color: #495057; }
        .suspect-info { color: #dc3545; font-weight: 600; }
        .post-link { text-decoration: none; color: #0d6efd; font-weight: 500; display: inline-flex; align-items: center; gap: 4px; }
        .post-link:hover { text-decoration: underline; }
        .badge-status { font-weight: 500; padding: 6px 10px; border-radius: 6px; min-width: 80px; }
    </style>
</head>
<body>

<nav class="navbar navbar-dark navbar-admin mb-4 px-3">
    <div class="container-fluid">
        <span class="navbar-brand mb-0 h1 fw-bold">
            <i class="bi bi-shield-lock-fill me-2"></i> 관리자 페이지
        </span>
        <a href="/" class="btn btn-outline-light btn-sm rounded-pill px-3">
            <i class="bi bi-box-arrow-right me-1"></i>메인으로
        </a>
    </div>
</nav>

<div class="container-fluid admin-container">
    
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold m-0 text-dark"><i class="bi bi-exclamation-triangle-fill text-warning me-2"></i>신고 접수 목록</h3>
        <div class="text-muted small">
            총 <span class="fw-bold text-primary">${fn:length(reports)}</span>건의 신고가 접수되었습니다.
        </div>
    </div>

    <div class="admin-card">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                    <tr style="height: 50px;">
                        <th style="width: 5%;">ID</th>
                        <th style="width: 12%;">신고자</th>
                        <th style="width: 12%;">피신고자</th>
                        <th style="width: 10%;">대상 글</th>
                        <th style="width: 25%;">신고 사유</th>
                        <th style="width: 12%;">접수일</th>
                        <th style="width: 10%;">상태</th>
                        <th style="width: 14%;">처리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty reports}">
                            <tr>
                                <td colspan="8" class="text-center py-5 text-muted">
                                    <div class="py-3">
                                        <i class="bi bi-check-circle fs-1 d-block mb-3 text-success opacity-50"></i>
                                        처리할 신고 내역이 없습니다. 깨끗하네요! ✨
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="report" items="${reports}">
                                <tr>
                                    <td class="text-center text-secondary small">${report.id}</td>
                                    
                                    <td class="text-center">
                                        <div class="reporter-info">
                                            <i class="bi bi-person me-1"></i>${report.reporterName}
                                            <div class="text-muted small" style="font-size: 0.75rem;">(ID: ${report.reporterId})</div>
                                        </div>
                                    </td>
                                    
                                    <td class="text-center">
                                        <div class="suspect-info">
                                            <i class="bi bi-person-x-fill me-1"></i>${report.suspectName}
                                            <div class="small opacity-75" style="font-size: 0.75rem;">(ID: ${report.suspectId})</div>
                                        </div>
                                    </td>
                                    
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${not empty report.postId}">
                                                <a href="/post/${report.postId}" target="_blank" class="post-link badge bg-light text-primary border">
                                                    Post #${report.postId} <i class="bi bi-box-arrow-up-right"></i>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted small">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    
                                    <td class="text-start">
                                        <div class="text-break bg-light p-2 rounded small border" style="max-height: 80px; overflow-y: auto;">
                                            ${report.reason}
                                        </div>
                                    </td>
                                    
                                    <td class="text-center text-muted small">
                                        ${fn:substring(fn:replace(report.createdAt, 'T', ' '), 0, 16)}
                                    </td>
                                    
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${report.status == 'PENDING'}">
                                                <span class="badge bg-warning text-dark badge-status"><i class="bi bi-hourglass-split me-1"></i>대기</span>
                                            </c:when>
                                            <c:when test="${report.status == 'APPROVED'}">
                                                <span class="badge bg-danger badge-status"><i class="bi bi-gavel me-1"></i>승인(정지)</span>
                                            </c:when>
                                            <c:when test="${report.status == 'REJECTED'}">
                                                <span class="badge bg-success badge-status"><i class="bi bi-check-lg me-1"></i>반려(무죄)</span>
                                            </c:when>
                                            <c:when test="${report.status == 'DUPLICATE'}">
                                                <span class="badge bg-secondary badge-status">중복</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    
                                    <td class="text-center">
                                        <form action="/admin/report/process" method="post">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                            <input type="hidden" name="id" value="${report.id}">
                                            
                                            <div class="input-group input-group-sm justify-content-center">
                                                <select name="status" class="form-select form-select-sm border-secondary" style="max-width: 100px;">
                                                    <option value="PENDING" ${report.status == 'PENDING' ? 'selected' : ''}>대기</option>
                                                    <option value="APPROVED" ${report.status == 'APPROVED' ? 'selected' : ''} class="text-danger fw-bold">승인</option>
                                                    <option value="REJECTED" ${report.status == 'REJECTED' ? 'selected' : ''} class="text-success fw-bold">반려</option>
                                                    <option value="DUPLICATE" ${report.status == 'DUPLICATE' ? 'selected' : ''}>중복</option>
                                                </select>
                                                <button type="submit" class="btn btn-dark btn-sm">변경</button>
                                            </div>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
    
    <div style="height: 100px;"></div>
</div>

<div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 11">
    <c:if test="${not empty message}">
        <div id="successToast" class="toast align-items-center text-bg-dark border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body fw-bold">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body fw-bold">
                    <i class="bi bi-exclamation-octagon-fill me-2"></i> ${error}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        <c:if test="${not empty message}">
            new bootstrap.Toast(document.getElementById('successToast'), { delay: 3000 }).show();
        </c:if>
        <c:if test="${not empty error}">
            new bootstrap.Toast(document.getElementById('errorToast'), { delay: 5000 }).show();
        </c:if>
    });
</script>

</body>
</html>