<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PoorDeal - 게시글 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa; /* 부드러운 회색 배경 */
        }
        .main-container {
            max-width: 1000px;
            margin: 0 auto;
        }
        .point-badge {
            background-color: #fff;
            border: 1px solid #dee2e6;
            border-radius: 20px;
            padding: 5px 15px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .table-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            overflow: hidden; /* 모서리 둥글게 유지 */
        }
        .table thead th {
            background-color: #f1f3f5;
            border-bottom: 2px solid #dee2e6;
            font-weight: 600;
            color: #495057;
        }
        .post-link:hover {
            color: #0d6efd !important;
            text-decoration: underline !important;
        }
        .badge-type {
            min-width: 60px;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom shadow-sm mb-5">
    <div class="container main-container">
        <a class="navbar-brand fw-bold text-primary" href="/">PoorDeal</a>
        
        <div class="collapse navbar-collapse justify-content-end">
            <div class="d-flex align-items-center gap-2">
                <sec:authorize access="isAnonymous()">
                    <a href="/login" class="btn btn-outline-primary btn-sm rounded-pill px-3">로그인</a>
                    <a href="/register" class="btn btn-primary btn-sm rounded-pill px-3">회원가입</a>
                </sec:authorize>

                <sec:authorize access="isAuthenticated()">
                    <sec:authorize access="hasAuthority('ROLE_ADMIN')">
                        <a href="/admin/report/list" class="btn btn-danger btn-sm rounded-pill me-2">
                            👮 신고 관리
                        </a>
                    </sec:authorize>

                    <div class="point-badge d-flex align-items-center me-2">
                        <span class="text-secondary small me-2">MY POINT</span>
                        <strong class="text-primary me-2">
                            <fmt:formatNumber value="${myPoint}" pattern="#,###"/> P
                        </strong>
                        <a href="/trade/charge" class="btn btn-success btn-sm py-0 px-2 rounded-pill" style="font-size: 0.75rem;">
                            ⚡ 충전
                        </a>
                    </div>

                    <div class="dropdown">
                        <button class="btn btn-light dropdown-toggle d-flex align-items-center gap-2 rounded-pill border" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <strong><sec:authentication property="principal.username"/></strong>님
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                            <li><a class="dropdown-item" href="/mail">📩 쪽지함</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <form action="/logout" method="post" class="px-2 py-1">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button type="submit" class="btn btn-outline-danger btn-sm w-100">로그아웃</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </div>
</nav>

<div class="container main-container pb-5">

    <div class="d-flex justify-content-between align-items-center mb-3 px-1">
        <h3 class="fw-bold m-0"><span class="text-primary">📢</span> 게시판</h3>
        <a href="/post/new" class="btn btn-primary rounded-pill px-4 shadow-sm">
            ✏️ 글쓰기
        </a>
    </div>

    <div class="table-container">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                    <tr style="height: 50px;">
                        <th style="width: 8%">번호</th>
                        <th style="width: 10%">분류</th>
                        <th style="width: 45%">제목</th>
                        <th style="width: 15%">작성자</th>
                        <th style="width: 22%">작성일</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty postList}">
                            <tr>
                                <td colspan="5" class="py-5 text-muted">
                                    <div class="py-4">
                                        <i class="bi bi-inbox fs-1 d-block mb-3"></i>
                                        등록된 게시글이 없습니다.
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="post" items="${postList}">
                                <tr>
                                    <td class="text-secondary small">${post.id}</td>
                                    
                                    <td>
                                        <c:choose>
                                            <c:when test="${post.type == 'TRADE'}">
                                                <span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 rounded-pill badge-type">거래</span>
                                            </c:when>
                                            <c:when test="${post.type == 'JOB'}">
                                                <span class="badge bg-warning bg-opacity-10 text-dark border border-warning border-opacity-25 rounded-pill badge-type">구인</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary bg-opacity-10 text-secondary border border-secondary border-opacity-25 rounded-pill badge-type">자유</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td class="text-start ps-4">
                                        <a href="/post/${post.id}" class="text-decoration-none text-dark fw-semibold post-link">
                                            ${post.title}
                                        </a>
                                    </td>
                                    <td>
                                        <span class="badge bg-light text-dark border rounded-pill px-2 fw-normal">
                                            ${post.author}
                                        </span>
                                    </td>
                                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
									
									<td class="text-center text-secondary small">
									    <%-- 'T'를 공백으로 바꾸고, 앞에서부터 16글자(년-월-일 시:분)만 자름 --%>
									    <c:out value="${fn:substring(fn:replace(post.createdAt, 'T', ' '), 0, 16)}"/>
									</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-4">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <li class="page-item ${startPage <= 1 ? 'disabled' : ''}">
                    <a class="page-link rounded-circle mx-1 border-0 shadow-sm" href="/?page=${startPage - 1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <c:forEach begin="${startPage}" end="${endPage}" var="p">
                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                        <a class="page-link rounded-circle mx-1 border-0 shadow-sm" href="/?page=${p}">${p}</a>
                    </li>
                </c:forEach>

                <li class="page-item ${endPage >= totalPages ? 'disabled' : ''}">
                    <a class="page-link rounded-circle mx-1 border-0 shadow-sm" href="/?page=${endPage + 1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
    
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>