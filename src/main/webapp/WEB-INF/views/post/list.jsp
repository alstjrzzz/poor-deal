<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PoorDeal - 게시글 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; }
        .main-container { max-width: 1000px; margin: 0 auto; }
        .point-badge { background-color: #fff; border: 1px solid #dee2e6; border-radius: 20px; padding: 5px 15px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        .table-container { background: white; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); overflow: hidden; }
        .table thead th { background-color: #f1f3f5; border-bottom: 2px solid #dee2e6; font-weight: 600; color: #495057; }
        .post-link:hover { color: #0d6efd !important; text-decoration: underline !important; }
        .badge-type { min-width: 60px; }
        .search-form .form-select { border-top-right-radius: 0; border-bottom-right-radius: 0; max-width: 120px; }
        .search-form .form-control { border-radius: 0; }
        .search-form .btn { border-top-left-radius: 0; border-bottom-left-radius: 0; }
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
                        <a href="/admin/report/list" class="btn btn-danger btn-sm rounded-pill me-2">👮 신고 관리</a>
                    </sec:authorize>

                    <div class="point-badge d-flex align-items-center me-2">
                        <span class="text-secondary small me-2">MY POINT</span>
                        <strong class="text-primary me-2"><fmt:formatNumber value="${myPoint}" pattern="#,###"/> P</strong>
                        <a href="/trade/charge" class="btn btn-success btn-sm py-0 px-2 rounded-pill" style="font-size: 0.75rem;">⚡ 충전</a>
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

    <div class="row align-items-center mb-3 g-2">
        <div class="col-md-auto me-auto">
            <h3 class="fw-bold m-0"><span class="text-primary">📢</span> 게시판</h3>
        </div>

        <div class="col-md-auto">
            <form action="/" method="get" class="d-flex search-form shadow-sm rounded">
                <select name="type" class="form-select border-0 bg-light">
                    <option value="" ${searchCondition.type == null ? 'selected' : ''}>전체</option>
                    <option value="FREE" ${searchCondition.type == 'FREE' ? 'selected' : ''}>자유</option>
                    <option value="TRADE" ${searchCondition.type == 'TRADE' ? 'selected' : ''}>거래</option>
                    <option value="JOB" ${searchCondition.type == 'JOB' ? 'selected' : ''}>구인/구직</option>
                </select>
                <input type="text" name="keyword" value="${searchCondition.keyword}" class="form-control border-0 bg-light border-start" placeholder="제목/내용 검색" style="width: 200px;">
                <button type="submit" class="btn btn-light border-0 text-primary fw-bold px-3 text-nowrap">
                    <i class="bi bi-search"></i> 검색
                </button>
            </form>
        </div>

        <div class="col-md-auto">
            <a href="/post/new" class="btn btn-primary rounded-pill px-4 shadow-sm">✏️ 글쓰기</a>
        </div>
    </div>

    <div class="table-container">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                    <tr style="height: 50px;">
                        <th style="width: 10%">분류</th>
                        <th style="width: 53%">제목</th>
                        <th style="width: 15%">작성자</th>
                        <th style="width: 22%">작성일</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty postList}">
                            <tr>
                                <td colspan="4" class="py-5 text-muted">
                                    <div class="py-4">
                                        <c:choose>
                                            <c:when test="${not empty searchCondition.type or not empty searchCondition.keyword}">
                                                <i class="bi bi-search fs-1 d-block mb-3"></i> 검색 결과가 없습니다.
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-inbox fs-1 d-block mb-3"></i> 등록된 게시글이 없습니다.
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="post" items="${postList}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${post.type == 'TRADE'}"><span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 rounded-pill badge-type">거래</span></c:when>
                                            <c:when test="${post.type == 'JOB'}"><span class="badge bg-warning bg-opacity-10 text-dark border border-warning border-opacity-25 rounded-pill badge-type">구인</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary bg-opacity-10 text-secondary border border-secondary border-opacity-25 rounded-pill badge-type">자유</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-start ps-4">
                                        <c:url var="detailUrl" value="/post/${post.id}">
                                            <c:param name="page" value="${currentPage}"/>
                                            <c:if test="${not empty searchCondition.type}"><c:param name="type" value="${searchCondition.type}"/></c:if>
                                            <c:if test="${not empty searchCondition.keyword}"><c:param name="keyword" value="${searchCondition.keyword}"/></c:if>
                                        </c:url>
                                        <a href="${detailUrl}" class="text-decoration-none text-dark fw-semibold post-link">${post.title}</a>
                                    </td>
                                    <td><span class="badge bg-light text-dark border rounded-pill px-2 fw-normal">${post.author}</span></td>
                                    <td class="text-center text-secondary small"><c:out value="${fn:substring(fn:replace(post.createdAt, 'T', ' '), 0, 16)}"/></td>
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
                <c:url var="prevUrl" value="/">
                    <c:param name="page" value="${startPage - 1}"/>
                    <c:if test="${not empty searchCondition.type}"><c:param name="type" value="${searchCondition.type}"/></c:if>
                    <c:if test="${not empty searchCondition.keyword}"><c:param name="keyword" value="${searchCondition.keyword}"/></c:if>
                </c:url>
                <li class="page-item ${startPage <= 1 ? 'disabled' : ''}">
                    <a class="page-link rounded-circle mx-1 border-0 shadow-sm" href="${prevUrl}">&laquo;</a>
                </li>
                <c:forEach begin="${startPage}" end="${endPage}" var="p">
                    <c:url var="pageUrl" value="/">
                        <c:param name="page" value="${p}"/>
                        <c:if test="${not empty searchCondition.type}"><c:param name="type" value="${searchCondition.type}"/></c:if>
                        <c:if test="${not empty searchCondition.keyword}"><c:param name="keyword" value="${searchCondition.keyword}"/></c:if>
                    </c:url>
                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                        <a class="page-link rounded-circle mx-1 border-0 shadow-sm" href="${pageUrl}">${p}</a>
                    </li>
                </c:forEach>
                <c:url var="nextUrl" value="/">
                    <c:param name="page" value="${endPage + 1}"/>
                    <c:if test="${not empty searchCondition.type}"><c:param name="type" value="${searchCondition.type}"/></c:if>
                    <c:if test="${not empty searchCondition.keyword}"><c:param name="keyword" value="${searchCondition.keyword}"/></c:if>
                </c:url>
                <li class="page-item ${endPage >= totalPages ? 'disabled' : ''}">
                    <a class="page-link rounded-circle mx-1 border-0 shadow-sm" href="${nextUrl}">&raquo;</a>
                </li>
            </ul>
        </nav>
    </div>
</div>

<div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 11">
    <c:if test="${not empty message}">
        <div id="successToast" class="toast align-items-center text-bg-primary border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body fw-semibold">
                    <i class="bi bi-check-circle-fill me-2"></i> ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body fw-semibold">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // 성공 메시지가 있으면 토스트 표시
        <c:if test="${not empty message}">
            const successToast = document.getElementById('successToast');
            const toast1 = new bootstrap.Toast(successToast, { delay: 3000 }); // 3초 뒤 사라짐
            toast1.show();
        </c:if>

        // 에러 메시지가 있으면 토스트 표시
        <c:if test="${not empty error}">
            const errorToast = document.getElementById('errorToast');
            const toast2 = new bootstrap.Toast(errorToast, { delay: 5000 }); // 5초 뒤 사라짐
            toast2.show();
        </c:if>
    });
</script>

</body>
</html>