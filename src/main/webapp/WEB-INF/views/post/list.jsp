<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">게시판</h2>
    
    <div class="d-flex justify-content-end mb-3">
        <a href="/post/new" class="btn btn-primary">글쓰기</a>
    </div>

    <table class="table table-hover table-bordered text-center">
        <thead class="table-light">
            <tr>
                <th style="width: 10%">번호</th>
                <th style="width: 50%">제목</th>
                <th style="width: 15%">작성자</th>
                <th style="width: 25%">작성일</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty postList}">
                    <tr>
                        <td colspan="4">등록된 게시글이 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="post" items="${postList}">
                        <tr>
                            <td>${post.id}</td>
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
    
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>
</div>
</body>
</html>