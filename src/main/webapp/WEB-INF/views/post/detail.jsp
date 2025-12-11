<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${post.title} - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .main-container {
            max-width: 900px;
            margin: 0 auto;
        }
        .post-card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.05);
            background: white;
            overflow: hidden;
        }
        .post-header {
            background-color: #fff;
            padding: 2rem 2rem 1rem 2rem;
            border-bottom: 1px solid #f1f3f5;
        }
        .post-body {
            padding: 2rem;
            min-height: 200px;
        }
        .comment-section {
            margin-top: 2rem;
        }
        .comment-card {
            border: none;
            background: #fff;
            border-radius: 0.75rem;
            margin-bottom: 1rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
        }
        .reply-card {
            background: #f8f9fa;
            border-radius: 0.75rem;
            margin-left: 3rem;
            margin-top: 0.5rem;
            padding: 1rem;
            border-left: 3px solid #dee2e6;
        }
        .deleted-comment {
            color: #adb5bd;
            font-style: italic;
        }
        .modified-text {
            font-size: 0.75rem;
            color: #adb5bd;
            margin-left: 5px;
        }
        .profile-icon {
            width: 35px;
            height: 35px;
            background-color: #e9ecef;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #495057;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="container main-container py-5">
    
    <div class="post-card mb-4">
        <div class="post-header">
            <div class="d-flex align-items-center mb-2">
                <c:choose>
                    <c:when test="${post.type == 'TRADE'}"><span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 rounded-pill px-3">거래</span></c:when>
                    <c:when test="${post.type == 'JOB'}"><span class="badge bg-warning bg-opacity-10 text-dark border border-warning border-opacity-25 rounded-pill px-3">구인</span></c:when>
                    <c:otherwise><span class="badge bg-secondary bg-opacity-10 text-secondary border border-secondary border-opacity-25 rounded-pill px-3">자유</span></c:otherwise>
                </c:choose>
                <span class="text-muted ms-auto small"><i class="bi bi-clock me-1"></i>${fn:substring(fn:replace(post.createdAt, 'T', ' '), 0, 16)}</span>
            </div>
            <h2 class="fw-bold mb-3 text-break">${post.title}</h2>
            
            <div class="d-flex align-items-center text-muted">
                <div class="profile-icon me-2">
                    <i class="bi bi-person-fill"></i>
                </div>
                <div>
                    <span class="fw-semibold text-dark">${post.author}</span>
                    <div class="small mt-1">
                        <c:if test="${post.type == 'TRADE'}">
                            <span class="text-primary fw-bold"><i class="bi bi-coin me-1"></i>${post.price}원</span>
                        </c:if>
                        <c:if test="${post.type == 'JOB'}">
                            <span class="text-success fw-bold"><i class="bi bi-people-fill me-1"></i>모집: ${post.filledCount} / ${post.hiringQuota} 명</span>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="post-body">
            <c:if test="${not empty post.image}">
                <div class="mb-4 text-center">
                    <img src="${post.image}?v=${post.updatedAt}" class="img-fluid rounded shadow-sm" alt="게시글 이미지" style="max-height: 500px;">
                </div>
            </c:if>
            
            <div class="card-text fs-6" style="white-space: pre-wrap; line-height: 1.7;">${post.content}</div>
        </div>
    </div>
    
    <div class="d-flex justify-content-between align-items-center mb-5">
        
        <div class="d-flex gap-2">
            <a href="/" class="btn btn-secondary rounded-pill px-4">
                <i class="bi bi-list me-1"></i> 목록
            </a>

            <sec:authorize access="isAuthenticated()">
                <c:if test="${loginId == post.authorId}">
                    <a href="/post/${post.id}/edit" class="btn btn-outline-primary rounded-pill px-3">
                        <i class="bi bi-pencil-square"></i> 수정
                    </a>
                    <form action="/post/${post.id}/delete" method="post" style="display:inline;" onsubmit="return confirm('정말 게시글을 삭제하시겠습니까?');">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <button type="submit" class="btn btn-outline-danger rounded-pill px-3">
                            <i class="bi bi-trash"></i> 삭제
                        </button>
                    </form>
                </c:if>
            </sec:authorize>
        </div>

        <sec:authorize access="isAuthenticated()">
            <c:if test="${loginId != post.authorId}">
                <div class="d-flex gap-2">
                    <a href="/report?suspectId=${post.authorId}&postId=${post.id}" class="btn btn-outline-danger rounded-pill px-3">
                        🚨 신고
                    </a>

                    <c:choose>
                        <c:when test="${isApplied}">
                              <button type="button" class="btn btn-secondary rounded-pill px-3" disabled>
                                <i class="bi bi-check-circle-fill me-1"></i> 요청 완료
                              </button>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${post.type == 'TRADE'}">
                                <button type="button" class="btn btn-success rounded-pill px-3 shadow-sm" data-bs-toggle="modal" data-bs-target="#requestModal">
                                    💸 거래 요청
                                </button>
                            </c:if>
                            <c:if test="${post.type == 'JOB'}">
                                <c:if test="${post.filledCount < post.hiringQuota}">
                                    <button type="button" class="btn btn-primary rounded-pill px-3 shadow-sm" data-bs-toggle="modal" data-bs-target="#requestModal">
                                        🤝 구직 신청
                                    </button>
                                </c:if>
                                <c:if test="${post.filledCount >= post.hiringQuota}">
                                    <button type="button" class="btn btn-secondary rounded-pill px-3" disabled>마감됨</button>
                                </c:if>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </sec:authorize>
    </div>

    <hr class="my-5 border-secondary border-opacity-25">
    
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-4">
            <h5 class="fw-bold mb-3"><i class="bi bi-chat-dots me-2"></i>댓글 작성</h5>
            <form action="/comment" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="parentType" value="POST">
                <input type="hidden" name="parentId" value="${post.id}">
                <div class="mb-3">
                    <textarea class="form-control bg-light border-0" name="content" rows="3" placeholder="소중한 댓글을 남겨주세요." required style="resize: none;"></textarea>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn btn-dark rounded-pill px-4 text-nowrap">등록</button>
                </div>
            </form>
        </div>
    </div>

    <h5 class="mb-3 fw-bold ms-1">댓글 <span class="text-primary">${totalCommentCount}</span>개</h5>
    
    <div class="comment-section">
        <c:forEach var="cr" items="${commentsAndReplies}">
            <div class="comment-card p-3">
                <div class="d-flex justify-content-between mb-2">
                    <div class="d-flex align-items-center">
                        <strong class="me-2">${cr.comment.author}</strong>
                        <small class="text-muted" style="font-size: 0.8rem;">
                            ${fn:substring(fn:replace(cr.comment.createdAt, 'T', ' '), 0, 16)}
                        </small>
                        <c:if test="${cr.comment.status == 'MODIFIED'}">
                            <span class="modified-text">(수정됨)</span>
                        </c:if>
                    </div>
                    
                    <c:if test="${cr.comment.status != 'DELETED' and loginId == cr.comment.authorId}">
                        <div class="dropdown">
                            <button class="btn btn-link text-secondary p-0" type="button" data-bs-toggle="dropdown">
                                <i class="bi bi-three-dots-vertical"></i>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                                <li><a class="dropdown-item" href="#" onclick="toggleEditForm('${cr.comment.id}'); return false;">수정</a></li>
                                <li>
                                    <form action="/comment/delete" method="post" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                        <input type="hidden" name="commentId" value="${cr.comment.id}">
                                        <input type="hidden" name="postId" value="${post.id}">
                                        <button type="submit" class="dropdown-item text-danger">삭제</button>
                                    </form>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                </div>

                <div id="comment-view-${cr.comment.id}">
                    <c:choose>
                        <c:when test="${cr.comment.status == 'DELETED'}">
                            <p class="deleted-comment mb-0"><i class="bi bi-exclamation-circle me-1"></i>삭제된 댓글입니다.</p>
                        </c:when>
                        <c:otherwise>
                            <p class="mb-1 text-dark" style="white-space: pre-wrap;">${cr.comment.content}</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div id="comment-edit-${cr.comment.id}" style="display:none;">
                    <form action="/comment/update" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <input type="hidden" name="commentId" value="${cr.comment.id}">
                        <input type="hidden" name="postId" value="${post.id}">
                        <textarea class="form-control mb-2" name="content" rows="2" required>${cr.comment.content}</textarea>
                        <div class="text-end">
                            <button type="button" class="btn btn-light btn-sm rounded-pill border" onclick="toggleEditForm('${cr.comment.id}')">취소</button>
                            <button type="submit" class="btn btn-primary btn-sm rounded-pill">수정 완료</button>
                        </div>
                    </form>
                </div>
                
                <sec:authorize access="isAuthenticated()">
                    <c:if test="${cr.comment.status != 'DELETED'}">
                        <button class="btn btn-link btn-sm text-decoration-none text-secondary ps-0 mt-1" type="button" onclick="toggleReplyForm('${cr.comment.id}')">
                            <i class="bi bi-arrow-return-right me-1"></i>답글 달기
                        </button>
                    </c:if>
                </sec:authorize>

                <div id="replyForm-${cr.comment.id}" class="mt-3 ps-3 border-start border-3" style="display:none;">
                    <form action="/comment" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <input type="hidden" name="parentType" value="COMMENT">
                        <input type="hidden" name="parentId" value="${cr.comment.id}">
                        <div class="d-flex gap-2">
                            <textarea class="form-control" name="content" rows="1" placeholder="답글을 입력하세요..." required style="resize: none;"></textarea>
                            <button type="submit" class="btn btn-primary btn-sm px-3 text-nowrap flex-shrink-0">등록</button>
                        </div>
                    </form>
                </div>
            </div>

            <c:if test="${not empty cr.replies}">
                <div class="ms-1">
                    <c:forEach var="reply" items="${cr.replies}">
                        <div class="reply-card">
                            <div class="d-flex justify-content-between mb-1">
                                <div>
                                    <strong class="small me-1">${reply.author}</strong>
                                    <small class="text-muted" style="font-size: 0.75rem;">
                                        ${fn:substring(fn:replace(reply.createdAt, 'T', ' '), 0, 16)}
                                    </small>
                                    <c:if test="${reply.status == 'MODIFIED'}">
                                        <span class="modified-text">(수정됨)</span>
                                    </c:if>
                                </div>
                                
                                <c:if test="${reply.status != 'DELETED' and loginId == reply.authorId}">
                                    <div class="d-flex gap-1">
                                        <a href="#" class="text-secondary small text-decoration-none" onclick="toggleEditForm('${reply.id}'); return false;">수정</a>
                                        <span class="text-secondary small">|</span>
                                        <form action="/comment/delete" method="post" style="display:inline;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                            <input type="hidden" name="commentId" value="${reply.id}">
                                            <input type="hidden" name="postId" value="${post.id}">
                                            <button type="submit" class="btn btn-link text-danger small p-0 text-decoration-none" style="vertical-align: baseline;">삭제</button>
                                        </form>
                                    </div>
                                </c:if>
                            </div>

                            <div id="comment-view-${reply.id}">
                                <c:choose>
                                    <c:when test="${reply.status == 'DELETED'}">
                                        <p class="deleted-comment mb-0 small">삭제된 댓글입니다.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="mb-0 small text-dark" style="white-space: pre-wrap;">${reply.content}</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div id="comment-edit-${reply.id}" class="mt-2" style="display:none;">
                                <form action="/comment/update" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    <input type="hidden" name="commentId" value="${reply.id}">
                                    <input type="hidden" name="postId" value="${post.id}">
                                    <textarea class="form-control mb-2 small" name="content" rows="2" required>${reply.content}</textarea>
                                    <div class="text-end">
                                        <button type="button" class="btn btn-light btn-sm border" onclick="toggleEditForm('${reply.id}')">취소</button>
                                        <button type="submit" class="btn btn-primary btn-sm">수정</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>

<div class="modal fade" id="requestModal" tabindex="-1" aria-labelledby="requestModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <form action="/mail/request" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                
                <input type="hidden" name="receiverId" value="${post.authorId}"> 
                <input type="hidden" name="postId" value="${post.id}">
                
                <c:choose>
                    <c:when test="${post.type == 'TRADE'}">
                        <input type="hidden" name="processType" value="TRADE">
                        <input type="hidden" name="amount" value="${fn:replace(post.price, ',', '')}"> 
                    </c:when>
                    <c:when test="${post.type == 'JOB'}">
                        <input type="hidden" name="processType" value="RECRUIT">
                        <input type="hidden" name="amount" value="0"> 
                    </c:when>
                </c:choose>

                <div class="modal-header bg-light border-bottom-0">
                    <h5 class="modal-title fw-bold" id="requestModalLabel">
                        <c:choose>
                            <c:when test="${post.type == 'TRADE'}">💸 거래 요청 보내기</c:when>
                            <c:when test="${post.type == 'JOB'}">🤝 구직 신청 보내기</c:when>
                        </c:choose>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label for="requestContent" class="form-label fw-bold small text-muted">메시지 내용</label>
                        <textarea class="form-control bg-light border-0" id="requestContent" name="content" rows="5" 
                            placeholder="상대방에게 보낼 메시지를 입력하세요.&#13;&#10;(예: 구매하고 싶습니다 / 알바 지원합니다)" required style="resize: none;"></textarea>
                    </div>
                </div>
                
                <div class="modal-footer border-top-0 pt-0 pb-4 pe-4">
                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4">보내기</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function toggleReplyForm(commentId) {
        const formDiv = document.getElementById('replyForm-' + commentId);
        if (formDiv.style.display === 'none') {
            formDiv.style.display = 'block';
        } else {
            formDiv.style.display = 'none';
        }
    }

    function toggleEditForm(commentId) {
        const viewDiv = document.getElementById('comment-view-' + commentId);
        const editDiv = document.getElementById('comment-edit-' + commentId);

        if (editDiv.style.display === 'none') {
            editDiv.style.display = 'block';
            viewDiv.style.display = 'none';
        } else {
            editDiv.style.display = 'none';
            viewDiv.style.display = 'block';
        }
    }
    
    <c:if test="${not empty errorMessage}">
        alert("⚠️ 오류: ${errorMessage}");
    </c:if>
    
    <c:if test="${not empty message}">
        alert("${message}");
    </c:if>
</script>
</body>
</html>