<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>내 쪽지함</title>
    <style>
        /* 레이아웃 및 기본 폰트 */
        body { width: 900px; margin: 0 auto; padding: 20px; font-family: 'Malgun Gothic', sans-serif; }

        /* 테이블 스타일 */
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: center; }
        th { background-color: #f8f9fa; color: #333; }
        tr:hover { background-color: #f1f1f1; }

        /* 페이지네이션 */
        .pagination { margin-top: 20px; text-align: center; }
        .pagination a { padding: 6px 12px; border: 1px solid #ddd; margin: 0 3px; text-decoration: none; color: #333; border-radius: 4px; }
        .pagination a.active { background-color: #007bff; color: white; border-color: #007bff; }
        .pagination a:hover:not(.active) { background-color: #ddd; }

        /* 메시지 알림 */
        .msg { color: #155724; background-color: #d4edda; border-color: #c3e6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }

        /* 헤더 박스 (제목 + 버튼) */
        .header-box { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 2px solid #eee; padding-bottom: 10px; }
        .header-box h2 { margin: 0; color: #333; }

        /* 버튼 스타일 */
        .btn-home { 
            padding: 8px 15px; 
            background-color: #6c757d; 
            color: white; 
            text-decoration: none; 
            border-radius: 4px; 
            font-size: 14px; 
            font-weight: bold;
            transition: background-color 0.2s;
        }
        .btn-home:hover { background-color: #5a6268; }

        /* 보낸 쪽지 배지 */
        .badge-sent { background-color: #e9ecef; color: #495057; font-size: 0.8em; padding: 2px 6px; border-radius: 4px; margin-right: 5px; font-weight: normal; }
    </style>
</head>
<body>

    <div class="header-box">
        <h2>📬 통합 쪽지함</h2>
        <a href="/" class="btn-home">🏠 글 목록으로</a>
    </div>
    
    <c:if test="${not empty message}">
        <p class="msg">✅ ${message}</p>
    </c:if>

    <table>
        <thead>
            <tr>
                <th style="width: 8%;">번호</th>
                <th style="width: 12%;">구분</th>
                <th>제목</th>
                <th style="width: 20%;">상대방</th> <th style="width: 20%;">날짜</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty mailList}">
                    <tr>
                        <td colspan="5" style="padding: 40px 0; color: #777;">
                            주고받은 쪽지가 없습니다.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="mail" items="${mailList}" varStatus="status">
                        <tr>
                            <td>${mail.id}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${mail.processType == 'TRADE'}"><span style="color:#28a745; font-weight:bold;">🛒 거래</span></c:when>
                                    <c:when test="${mail.processType == 'RECRUIT'}"><span style="color:#ffc107; font-weight:bold; text-shadow: 0px 0px 1px #999;">🤝 구인</span></c:when>
                                    <c:otherwise><span style="color:#6c757d;">📢 일반</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td style="text-align: left; padding-left: 20px;">
                                <a href="/mail/${mail.id}" style="text-decoration: none; color: #333; font-weight: bold;">
                                    <%-- 내가 보낸 메일이면 배지 표시 --%>
                                    <c:if test="${mail.senderId == loginId}">
                                        <span class="badge-sent">[보낸 쪽지]</span>
                                    </c:if>
                                    ${mail.title}
                                </a>
                            </td>
                            <td>
                                <%-- 내가 보낸거면 '👉 받는사람', 내가 받은거면 '보낸사람' 표시 --%>
                                <c:choose>
                                    <c:when test="${mail.senderId == loginId}">
                                        <span style="color: #666;">👉 ${mail.receiver}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <strong>${mail.sender}</strong>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td style="font-size: 0.9em; color: #666;">
                                <fmt:parseDate value="${mail.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
                                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <div class="pagination">
        <c:if test="${startPage > 1}">
            <a href="/mail?page=${startPage - 1}">&laquo; 이전</a>
        </c:if>

        <c:forEach begin="${startPage}" end="${endPage}" var="p">
            <a href="/mail?page=${p}" class="${p == currentPage ? 'active' : ''}">${p}</a>
        </c:forEach>

        <c:if test="${endPage < totalPages}">
            <a href="/mail?page=${endPage + 1}">다음 &raquo;</a>
        </c:if>
    </div>

</body>
</html>