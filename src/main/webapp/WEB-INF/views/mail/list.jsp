<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>내 쪽지함 - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .main-container {
            max-width: 900px;
            margin: 50px auto;
        }
        .mail-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            background: white;
            overflow: hidden;
        }
        .table thead th {
            background-color: #f1f3f5;
            border-bottom: 2px solid #dee2e6;
            font-weight: 600;
            color: #495057;
            text-align: center;
            vertical-align: middle;
        }
        .table tbody td {
            vertical-align: middle;
        }
        .mail-link {
            display: block;
            color: #212529;
            text-decoration: none;
            font-weight: 500;
        }
        .mail-link:hover {
            color: #0d6efd;
        }
        .pagination .page-link {
            border: none;
            color: #495057;
            margin: 0 3px;
            border-radius: 50%;
            width: 35px;
            height: 35px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .pagination .page-item.active .page-link {
            background-color: #0d6efd;
            color: white;
            font-weight: bold;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-light bg-white border-bottom mb-4 py-1">
    <div class="container main-container">
        <a class="navbar-brand fw-bold text-primary d-flex align-items-center" href="/">
            <i class="bi bi-arrow-left me-2"></i>메인으로
        </a>
        <span class="navbar-text small">
            <strong><sec:authentication property="principal.username"/></strong>님의 쪽지함 📬
        </span>
    </div>
</nav>

<div class="container main-container">
    
    <div class="d-flex justify-content-between align-items-end mb-3 px-1">
        <h3 class="fw-bold m-0">통합 쪽지함</h3>
        <small class="text-muted">주고받은 모든 쪽지를 확인할 수 있습니다.</small>
    </div>

    <div class="mail-card">
        
        <c:if test="${not empty message}">
            <div class="alert alert-success m-3 d-flex align-items-center py-2" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <div>${message}</div>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-hover mb-0">
                <thead>
                    <tr style="height: 45px;">
                        <th style="width: 8%">번호</th>
                        <th style="width: 12%">구분</th>
                        <th style="width: 40%">제목</th>
                        <th style="width: 25%">상대방</th>
                        <th style="width: 15%">날짜</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty mailList}">
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted">
                                    <div class="py-4">
                                        <i class="bi bi-envelope-open fs-1 d-block mb-3 opacity-50"></i>
                                        주고받은 쪽지가 없습니다.
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="mail" items="${mailList}">
                                <tr>
                                    <td class="text-center text-secondary small">${mail.id}</td>
                                    
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${mail.processType == 'TRADE'}">
                                                <span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 rounded-pill px-2">🛒 거래</span>
                                            </c:when>
                                            <c:when test="${mail.processType == 'RECRUIT'}">
                                                <span class="badge bg-warning bg-opacity-10 text-dark border border-warning border-opacity-25 rounded-pill px-2">🤝 구인</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-light text-secondary border px-2">📢 일반</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <a href="/mail/${mail.id}" class="mail-link text-truncate" style="max-width: 300px;">
                                            <c:if test="${mail.senderId == loginId}">
                                                <span class="badge bg-secondary me-1" style="font-size: 0.7rem;">MY</span>
                                            </c:if>
                                            ${mail.title}
                                        </a>
                                    </td>

                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${mail.senderId == loginId}">
                                                <span class="text-muted small">
                                                    <i class="bi bi-arrow-right-short text-primary"></i> ${mail.receiver}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="d-flex align-items-center justify-content-center">
                                                    <i class="bi bi-person-circle me-1 text-secondary"></i>
                                                    <span class="fw-semibold text-dark">${mail.sender}</span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td class="text-center text-muted small">
                                        ${fn:substring(fn:replace(mail.createdAt, 'T', ' '), 0, 16)}
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <c:if test="${not empty mailList}">
            <div class="p-3 border-top">
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center mb-0">
                        
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link shadow-sm" href="/mail?page=1" aria-label="First">
                                <span aria-hidden="true"><i class="bi bi-chevron-double-left"></i></span>
                            </a>
                        </li>

                        <c:forEach begin="${startPage}" end="${endPage}" var="p">
                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                <a class="page-link shadow-sm" href="/mail?page=${p}">${p}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link shadow-sm" href="/mail?page=${totalPages}" aria-label="Last">
                                <span aria-hidden="true"><i class="bi bi-chevron-double-right"></i></span>
                            </a>
                        </li>
                        
                    </ul>
                </nav>
            </div>
        </c:if>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>