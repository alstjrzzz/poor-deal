<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>쪽지 상세보기 - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .main-container {
            max-width: 800px;
            margin: 50px auto;
        }
        .detail-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            background: white;
            overflow: hidden;
        }
        .card-header-custom {
            background-color: #fff;
            border-bottom: 1px solid #f1f3f5;
            padding: 1.5rem;
        }
        .info-grid {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 1.2rem;
            margin-bottom: 1.5rem;
        }
        .content-box {
            min-height: 150px;
            white-space: pre-wrap;
            line-height: 1.6;
            color: #333;
            padding: 1rem;
        }
        .action-card {
            background-color: #eff6ff; /* 아주 연한 파란색 */
            border: 1px solid #dbeafe;
            border-radius: 12px;
            padding: 1.5rem;
            margin-top: 2rem;
        }
        .guide-text {
            color: #1e40af;
            font-weight: 500;
            margin-bottom: 1rem;
            font-size: 0.95rem;
        }
        .form-label {
            font-size: 0.9rem;
            font-weight: 600;
            color: #495057;
        }
    </style>
</head>
<body>

<div class="container main-container">

    <%-- 1. 상태 코드를 한글로 변환하는 로직 (기존 유지) --%>
    <c:set var="statusKorean">
        <c:choose>
            <c:when test="${mail.processStatus == 'TRADE_REQUEST_PENDING'}">구매자 요청 대기</c:when>
            <c:when test="${mail.processStatus == 'TRADE_CONDITIONS_SET'}">판매자 조건 제시됨</c:when>
            <c:when test="${mail.processStatus == 'TRADE_ACCEPTED'}">거래 수락됨 (진행 중)</c:when>
            <c:when test="${mail.processStatus == 'TRADE_REJECTED'}">거래 거절됨</c:when>
            <c:when test="${mail.processStatus == 'TRADE_TRANSFER_PENDING'}">송금 대기 중</c:when>
            <c:when test="${mail.processStatus == 'TRADE_PROCESS_COMPLETED'}">거래 완료 🎉</c:when>
            
            <c:when test="${mail.processStatus == 'RECRUIT_REQUEST_PENDING'}">구직 요청 대기</c:when>
            <c:when test="${mail.processStatus == 'RECRUIT_ACCEPTED'}">채용 수락됨 ✅</c:when>
            <c:when test="${mail.processStatus == 'RECRUIT_REJECTED'}">채용 거절됨 ❌</c:when>

            <c:when test="${mail.processStatus == 'PROCESSED'}">처리 완료 (이전 단계)</c:when>
            <c:otherwise>${mail.processStatus}</c:otherwise>
        </c:choose>
    </c:set>

    <%-- 상태별 배지 색상 결정 --%>
    <c:set var="badgeClass">
        <c:choose>
            <c:when test="${mail.processStatus.name().contains('PENDING')}">bg-warning text-dark</c:when>
            <c:when test="${mail.processStatus.name().contains('ACCEPTED') or mail.processStatus.name().contains('COMPLETED')}">bg-success</c:when>
            <c:when test="${mail.processStatus.name().contains('REJECTED')}">bg-danger</c:when>
            <c:otherwise>bg-secondary</c:otherwise>
        </c:choose>
    </c:set>

    <div class="detail-card">
        <div class="card-header-custom d-flex justify-content-between align-items-center">
            <div>
                <div class="mb-2">
                    <c:choose>
                        <c:when test="${mail.processType == 'TRADE'}"><span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 rounded-pill">거래</span></c:when>
                        <c:when test="${mail.processType == 'RECRUIT'}"><span class="badge bg-warning bg-opacity-10 text-dark border border-warning border-opacity-25 rounded-pill">구인</span></c:when>
                        <c:otherwise><span class="badge bg-secondary bg-opacity-10 text-secondary border border-secondary border-opacity-25 rounded-pill">일반</span></c:otherwise>
                    </c:choose>
                    <span class="badge ${badgeClass} rounded-pill ms-1">${statusKorean}</span>
                </div>
                <h4 class="fw-bold m-0 text-break">${mail.title}</h4>
            </div>
            
            <c:if test="${not empty mail.postId}">
                <a href="/post/${mail.postId}" class="btn btn-outline-primary btn-sm rounded-pill px-3 flex-shrink-0" target="_blank">
                    <i class="bi bi-box-arrow-up-right me-1"></i>게시글 보기
                </a>
            </c:if>
        </div>

        <div class="p-4">
            <div class="info-grid">
                <div class="row g-3">
                    <div class="col-md-6">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-send text-secondary fs-5 me-3"></i>
                            <div>
                                <small class="text-muted d-block">보낸 사람</small>
                                <span class="fw-semibold">${mail.sender}</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-person-check text-secondary fs-5 me-3"></i>
                            <div>
                                <small class="text-muted d-block">받은 사람</small>
                                <span class="fw-semibold">${mail.receiver}</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-calendar-event text-secondary fs-5 me-3"></i>
                            <div>
                                <small class="text-muted d-block">보낸 날짜</small>
                                <span>
                                    <fmt:parseDate value="${mail.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
                                    <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
                                </span>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not empty mail.amount && mail.amount > 0}">
                        <div class="col-md-6">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-cash-coin text-primary fs-5 me-3"></i>
                                <div>
                                    <small class="text-muted d-block">관련 금액</small>
                                    <strong class="text-primary fs-5"><fmt:formatNumber value="${mail.amount}" type="currency"/></strong>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="content-box">
                ${mail.content}
            </div>

            <%-- ================= 액션 영역 (받는 사람인 경우에만 표시) ================= --%>
            <c:if test="${mail.receiverId == loginId}">
                
                <c:if test="${mail.processStatus != 'PROCESSED' && 
                              !mail.processStatus.name().contains('COMPLETED') && 
                              !mail.processStatus.name().contains('ACCEPTED') && 
                              !mail.processStatus.name().contains('REJECTED')}">

                    <div class="action-card shadow-sm">
                        <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-lightning-charge-fill me-1"></i> 작업 수행</h5>
                        
                        <form action="/mail/action" method="post">
                            <input type="hidden" name="mailId" value="${mail.id}">
                            <input type="hidden" name="postId" value="${mail.postId}">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                            <%-- 공통 메시지 입력 --%>
                            <div class="mb-3">
                                <label class="form-label">답장 메시지 (선택)</label>
                                <textarea class="form-control" name="content" rows="3" placeholder="상대방에게 보낼 메시지를 입력하세요."></textarea>
                            </div>
                            
                            <hr class="border-primary opacity-25 my-4">

                            <%-- 거래(Trade) 관련 액션 --%>
                            <c:if test="${mail.processType == 'TRADE'}">
                                
                                <c:if test="${mail.processStatus == 'TRADE_REQUEST_PENDING'}">
                                    <div class="guide-text"><i class="bi bi-info-circle-fill me-1"></i>구매자가 거래를 원합니다. 거래 시간과 장소를 제안해주세요.</div>
                                    
                                    <div class="row g-3 mb-3">
                                        <div class="col-md-6">
                                            <label class="form-label">거래 시간</label>
                                            <input type="text" class="form-control" name="tradeTime" placeholder="예: 내일 오후 6시" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">거래 장소</label>
                                            <input type="text" class="form-control" name="tradeLocation" placeholder="예: 정문 시계탑 앞" required>
                                        </div>
                                    </div>
                                    
                                    <button type="submit" name="actionType" value="SET_CONDITIONS" class="btn btn-primary w-100 fw-bold">
                                        <i class="bi bi-send me-1"></i>조건 보내기
                                    </button>
                                </c:if>

                                <c:if test="${mail.processStatus == 'TRADE_CONDITIONS_SET'}">
                                    <div class="guide-text text-center"><i class="bi bi-question-circle-fill me-1"></i>판매자가 거래 조건을 제시했습니다. 이 조건으로 거래하시겠습니까?</div>
                                    <div class="d-flex gap-2 justify-content-center">
                                        <button type="submit" name="actionType" value="ACCEPT_TRADE" class="btn btn-success px-4 fw-bold"><i class="bi bi-check-lg me-1"></i>수락하기</button>
                                        <button type="submit" name="actionType" value="REJECT_TRADE" class="btn btn-danger px-4 fw-bold"><i class="bi bi-x-lg me-1"></i>거절하기</button>
                                    </div>
                                </c:if>

                                <c:if test="${mail.processStatus == 'TRADE_TRANSFER_PENDING'}">
                                    <div class="alert alert-info border-info d-flex align-items-center mb-3">
                                        <i class="bi bi-exclamation-circle-fill fs-4 me-3 text-info"></i>
                                        <div>
                                            거래가 확정되었습니다. 아래 금액을 송금해주세요.<br>
                                            <strong>송금액: <fmt:formatNumber value="${mail.amount}" type="currency"/></strong>
                                        </div>
                                    </div>
                                    <button type="submit" name="actionType" value="TRANSFER" class="btn btn-primary w-100 fw-bold">
                                        <i class="bi bi-cash-coin me-1"></i>송금하기
                                    </button>
                                </c:if>

                            </c:if>

                            <%-- 구인(Job) 관련 액션 --%>
                            <c:if test="${mail.processType == 'RECRUIT'}">
                                <c:if test="${mail.processStatus == 'RECRUIT_REQUEST_PENDING'}">
                                    <div class="guide-text text-center"><i class="bi bi-person-plus-fill me-1"></i>이 지원자를 채용하시겠습니까?</div>
                                    <div class="d-flex gap-2 justify-content-center">
                                        <button type="submit" name="actionType" value="ACCEPT_RECRUIT" class="btn btn-success px-4 fw-bold"><i class="bi bi-check-lg me-1"></i>채용하기</button>
                                        <button type="submit" name="actionType" value="REJECT_RECRUIT" class="btn btn-danger px-4 fw-bold"><i class="bi bi-x-lg me-1"></i>거절하기</button>
                                    </div>
                                </c:if>
                            </c:if>

                        </form>
                    </div>
                </c:if>
            </c:if>

            <%-- ================= 상태 메시지 영역 ================= --%>
            <div class="mt-4">
                <c:if test="${mail.processStatus == 'PROCESSED'}">
                    <div class="alert alert-secondary text-center fw-bold shadow-sm">
                        <i class="bi bi-check2-all me-2"></i>이미 답장(처리)하여 완료된 단계입니다.
                    </div>
                </c:if>
                <c:if test="${mail.processStatus.name().contains('COMPLETED') || mail.processStatus.name().contains('ACCEPTED')}">
                    <div class="alert alert-success text-center fw-bold shadow-sm">
                        <i class="bi bi-check-circle-fill me-2"></i>최종 처리가 완료된 건입니다.
                    </div>
                </c:if>
                <c:if test="${mail.processStatus.name().contains('REJECTED')}">
                    <div class="alert alert-danger text-center fw-bold shadow-sm">
                        <i class="bi bi-x-circle-fill me-2"></i>거절되거나 취소된 건입니다.
                    </div>
                </c:if>
                
                <%-- 보낸 사람 시점: 대기 중 메시지 --%>
                <c:if test="${mail.senderId == loginId && 
                              mail.processStatus != 'PROCESSED' &&
                              !mail.processStatus.name().contains('COMPLETED') && 
                              !mail.processStatus.name().contains('ACCEPTED') && 
                              !mail.processStatus.name().contains('REJECTED')}">
                    <div class="alert alert-light border text-center text-muted shadow-sm">
                        <div class="spinner-border spinner-border-sm text-secondary me-2" role="status"></div>
                        <strong>상대방(${mail.receiver})의 응답을 기다리는 중입니다.</strong><br>
                        <small>상대방이 확인하고 조치를 취하면 상태가 변경됩니다.</small>
                    </div>
                </c:if>
            </div>

            <div class="d-grid mt-4">
                <a href="/mail" class="btn btn-light border py-2 text-secondary fw-bold">
                    목록으로 돌아가기
                </a>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<%-- 알림 스크립트 --%>
<script>
    <c:if test="${not empty message}">
        alert("${message}");
    </c:if>

    <c:if test="${not empty error}">
        alert("${error}");
    </c:if>
</script>

</body>
</html>