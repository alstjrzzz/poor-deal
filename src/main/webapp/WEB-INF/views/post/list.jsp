<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    
    <%-- 상단 헤더 영역 (로그인/로그아웃/정보표시) --%>
    <div class="d-flex justify-content-end mb-4">
        <sec:authorize access="isAnonymous()">
            <a href="/login" class="btn btn-outline-primary me-2">로그인</a>
            <a href="/register" class="btn btn-primary">회원가입</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <div class="d-flex align-items-center">
                <span class="me-3">
                    <strong><sec:authentication property="principal.username"/></strong>님 환영합니다!
                </span>
                
                <%-- [추가] 포인트 정보 및 충전 버튼 --%>
                <div class="d-flex align-items-center me-3 px-3 py-1 border rounded bg-light">
                    <span class="text-secondary me-2" style="font-size: 0.9rem;">내 포인트:</span>
                    <strong class="text-primary me-2">
                        <fmt:formatNumber value="${myPoint}" pattern="#,###"/> P
                    </strong>
                    <a href="/trade/charge" class="btn btn-sm btn-success" style="font-size: 0.75rem; padding: 2px 8px; font-weight: bold;">
                        ⚡ 충전
                    </a>
                </div>
                
                <a href="/mail" class="btn btn-outline-secondary me-2">
                    쪽지함
                </a>

                <form action="/logout" method="post" style="display:inline;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-outline-danger">로그아웃</button>
                </form>
            </div>
        </sec:authorize>
    </div>

    <h2 class="mb-4">게시판</h2>
    
    <div class="d-flex justify-content-end mb-3">
        <a href="/post/new" class="btn btn-primary">글쓰기</a>
    </div>

    <table class="table table-hover table-bordered text-center">
        <thead class="table-light">
            <tr>
                <th style="width: 10%">번호</th>
                <th style="width: 10%">분류</th>
                <th style="width: 40%">제목</th>
                <th style="width: 15%">작성자</th>
                <th style="width: 25%">작성일</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty postList}">
                    <tr>
                        <td colspan="5">등록된 게시글이 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="post" items="${postList}">
                        <tr>
                            <td>${post.id}</td>
                            
                            <td>
                                <c:choose>
                                    <c:when test="${post.type == 'TRADE'}">
                                        <span class="badge bg-success">거래</span>
                                    </c:when>
                                    <c:when test="${post.type == 'JOB'}">
                                        <span class="badge bg-warning text-dark">구인</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">자유</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="text-start">
                                <a href="/post/${post.id}" class="text-decoration-none text-dark">
                                    ${post.title}
                                </a>
                            </td>
                            <td>${post.author}</td>
                            <td>
                                <c:out value="${post.createdAt}"/> 
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <%-- 페이지네이션 --%>
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <li class="page-item ${startPage <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="/?page=${startPage - 1}" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>

            <c:forEach begin="${startPage}" end="${endPage}" var="p">
                <li class="page-item ${p == currentPage ? 'active' : ''}">
                    <a class="page-link" href="/?page=${p}">${p}</a>
                </li>
            </c:forEach>

            <li class="page-item ${endPage >= totalPages ? 'disabled' : ''}">
                <a class="page-link" href="/?page=${endPage + 1}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
    
    <%-- 알림 메시지 스크립트 --%>
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>
</div>
</body>
</html>