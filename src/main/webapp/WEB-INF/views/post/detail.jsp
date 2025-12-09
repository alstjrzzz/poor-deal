<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${post.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5 mb-5" style="max-width: 900px;">
    
    <div class="card mb-4">
        <div class="card-header d-flex justify-content-between align-items-center">
            <h4 class="mb-0">
                <c:choose>
                    <c:when test="${post.type == 'TRADE'}"><span class="badge bg-success">거래</span></c:when>
                    <c:when test="${post.type == 'JOB'}"><span class="badge bg-warning text-dark">구인</span></c:when>
                    <c:otherwise><span class="badge bg-secondary">자유</span></c:otherwise>
                </c:choose>
                ${post.title}
            </h4>
            <small class="text-muted">${post.createdAt}</small>
        </div>
        <div class="card-body">
            <h6 class="card-subtitle mb-3 text-muted">작성자: ${post.author}</h6>
            
            <c:if test="${post.type == 'TRADE'}">
                <p class="fw-bold text-primary">가격: ${post.price}원</p>
            </c:if>
            <c:if test="${post.type == 'JOB'}">
                <p class="fw-bold text-success">모집 현황: ${post.filledCount} / ${post.hiringQuota} 명</p>
            </c:if>
            
            <c:if test="${not empty post.image}">
                <div class="mb-3 text-center">
                    <img src="${post.image}" class="img-fluid rounded" alt="게시글 이미지" style="max-height: 500px;">
                </div>
            </c:if>
            
            <div class="card-text" style="white-space: pre-wrap;">${post.content}</div>
        </div>
    </div>
    
    <div class="d-flex justify-content-between mb-3">
        <a href="/" class="btn btn-secondary">목록으로</a>

        <sec:authorize access="isAuthenticated()">
            <%-- [수정] 작성자가 아닐 경우에만 신청 버튼 표시 --%>
            <c:if test="${loginId != post.authorId}">
                
                <c:choose>
                    <%-- 1. 이미 신청한 경우: 비활성화된 버튼 표시 --%>
                    <c:when test="${isApplied}">
                         <button type="button" class="btn btn-secondary" disabled>
                            ✅ 이미 요청을 보냈습니다
                        </button>
                    </c:when>
                    
                    <%-- 2. 신청하지 않은 경우: 정상 버튼 표시 --%>
                    <c:otherwise>
                        <c:if test="${post.type == 'TRADE'}">
                            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#requestModal">
                                💸 거래 요청하기
                            </button>
                        </c:if>
                        
                        <c:if test="${post.type == 'JOB'}">
                            <c:if test="${post.filledCount < post.hiringQuota}">
                                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#requestModal">
                                    🤝 구직 신청하기
                                </button>
                            </c:if>
                            <c:if test="${post.filledCount >= post.hiringQuota}">
                                <button type="button" class="btn btn-secondary" disabled>마감됨</button>
                            </c:if>
                        </c:if>
                    </c:otherwise>
                </c:choose>
                
            </c:if>
        </sec:authorize>
    </div>

    <hr>

    <div class="card mb-4 bg-light">
        <div class="card-body">
            <form action="/comment" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="parentType" value="POST">
                <input type="hidden" name="parentId" value="${post.id}">
                <div class="mb-3">
                    <label class="form-label fw-bold">댓글 작성</label>
                    <textarea class="form-control" name="content" rows="3" required></textarea>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn btn-dark btn-sm">등록</button>
                </div>
            </form>
        </div>
    </div>

    <h5 class="mb-3">댓글 (${totalCommentCount}개)</h5>
    
    <c:forEach var="cr" items="${commentsAndReplies}">
        <div class="card mb-3 border-0">
            <div class="card-body border-bottom">
                <div class="d-flex justify-content-between">
                    <strong>${cr.comment.author}</strong>
                    <small class="text-muted">${cr.comment.createdAt}</small>
                </div>
                <p class="mt-2 mb-2">${cr.comment.content}</p>
                
                <sec:authorize access="isAuthenticated()">
                    <button class="btn btn-outline-secondary btn-sm" type="button" onclick="toggleReplyForm('${cr.comment.id}')">
                        답글 달기
                    </button>
                </sec:authorize>

                <div id="replyForm-${cr.comment.id}" class="mt-3" style="display:none;">
                    <form action="/comment" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <input type="hidden" name="parentType" value="COMMENT">
                        <input type="hidden" name="parentId" value="${cr.comment.id}">
                        <div class="d-flex">
                            <textarea class="form-control me-2" name="content" rows="1" placeholder="답글을 입력하세요..." required></textarea>
                            <button type="submit" class="btn btn-primary btn-sm">등록</button>
                        </div>
                    </form>
                </div>
            </div>

            <c:if test="${not empty cr.replies}">
                <div class="bg-light p-3 ms-4 rounded-bottom">
                    <c:forEach var="reply" items="${cr.replies}">
                        <div class="border-bottom mb-2 pb-2">
                            <div class="d-flex justify-content-between">
                                <strong>ㄴ ${reply.author}</strong>
                                <small class="text-muted">${reply.createdAt}</small>
                            </div>
                            <p class="mb-0 mt-1">${reply.content}</p>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </c:forEach>
</div>

<div class="modal fade" id="requestModal" tabindex="-1" aria-labelledby="requestModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/mail/request" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                
                <input type="hidden" name="receiverId" value="${post.authorId}"> <input type="hidden" name="postId" value="${post.id}">
                
                <c:choose>
                    <c:when test="${post.type == 'TRADE'}">
                        <input type="hidden" name="processType" value="TRADE">
                        <input type="hidden" name="amount" value="${fn:replace(post.price, ',', '')}"> 
                    </c:when>
                    <c:when test="${post.type == 'JOB'}">
                        <input type="hidden" name="processType" value="RECRUIT">
                        <input type="hidden" name="amount" value="0"> </c:when>
                </c:choose>

                <div class="modal-header">
                    <h5 class="modal-title" id="requestModalLabel">
                        <c:choose>
                            <c:when test="${post.type == 'TRADE'}">💸 거래 요청 보내기</c:when>
                            <c:when test="${post.type == 'JOB'}">🤝 구직 신청 보내기</c:when>
                        </c:choose>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="requestContent" class="form-label">메시지 내용</label>
                        <textarea class="form-control" id="requestContent" name="content" rows="4" 
                            placeholder="상대방에게 보낼 메시지를 입력하세요.&#13;&#10;(예: 구매하고 싶습니다 / 알바 지원합니다)" required></textarea>
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary">보내기</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // 대댓글 폼 토글 함수
    function toggleReplyForm(commentId) {
        const formDiv = document.getElementById('replyForm-' + commentId);
        if (formDiv.style.display === 'none') {
            formDiv.style.display = 'block';
        } else {
            formDiv.style.display = 'none';
        }
    }
    
    <c:if test="${not empty errorMessage}">
        alert("⚠️ 오류: ${errorMessage}");
    </c:if>
    
    // 성공 메시지 처리
    <c:if test="${not empty message}">
        alert("${message}");
    </c:if>
</script>
</body>
</html>