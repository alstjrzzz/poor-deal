<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>쪽지 상세보기</title>
    <style>
        .mail-container { border: 1px solid #ccc; padding: 30px; width: 700px; margin: 30px auto; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); font-family: 'Malgun Gothic', sans-serif;}
        .mail-header { border-bottom: 2px solid #eee; padding-bottom: 20px; margin-bottom: 20px; }
        
        .header-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
        .header-top h3 { margin: 0; font-size: 1.5rem; }

        .info-row { margin: 8px 0; color: #555; font-size: 1em; }
        .info-label { display: inline-block; width: 80px; font-weight: bold; color: #333; }
        
        .mail-content { 
            min-height: 200px; 
            white-space: pre-wrap; 
            background: #f8f9fa; 
            padding: 25px; 
            border-radius: 8px; 
            border: 1px solid #e9ecef;
            line-height: 1.6;
        }

        .action-box { 
            margin-top: 30px; 
            padding: 25px; 
            background-color: #e3f2fd; 
            border: 1px solid #90caf9; 
            border-radius: 8px; 
        }
        .action-box h4 { margin-top: 0; color: #1565c0; margin-bottom: 15px; }
        .guide-text { font-size: 1em; color: #333; margin-bottom: 20px; font-weight: 500; }

        /* 버튼 스타일 */
        .btn { padding: 8px 16px; cursor: pointer; border: none; border-radius: 4px; color: white; font-weight: bold; margin-right: 5px; text-decoration: none; display: inline-block; font-size: 14px; transition: 0.2s; }
        .btn-primary { background-color: #007bff; } .btn-primary:hover { background-color: #0056b3; }
        .btn-success { background-color: #28a745; } .btn-success:hover { background-color: #218838; }
        .btn-danger { background-color: #dc3545; } .btn-danger:hover { background-color: #c82333; }
        .btn-secondary { background-color: #6c757d; } .btn-secondary:hover { background-color: #545b62; }
        .btn-info { background-color: #17a2b8; } .btn-info:hover { background-color: #138496; }

        .btn-list { background-color: #6c757d; display: block; width: 120px; margin: 30px auto 0; text-align: center; padding: 10px; }

        /* 입력 폼 스타일 */
        .input-group { margin-bottom: 15px; }
        .input-group label { display: block; font-weight: bold; margin-bottom: 8px; font-size: 0.95em; }
        .input-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        
        /* [추가] 텍스트 영역 스타일 */
        .input-group textarea { 
            width: 100%; 
            padding: 10px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
            box-sizing: border-box; 
            resize: vertical; /* 세로 크기 조절 허용 */
            min-height: 80px;
            font-family: inherit;
        }
        
        /* 상태 배지 */
        .status-badge { 
            display: inline-block; padding: 5px 12px; border-radius: 20px; 
            font-size: 13px; color: white; background-color: #6c757d; vertical-align: middle; font-weight: bold;
        }
        .status-PENDING { background-color: #ffc107; color: #333; }
        .status-ACCEPTED, .status-COMPLETED { background-color: #28a745; } 
        .status-REJECTED { background-color: #dc3545; }
    </style>
</head>
<body>

<div class="mail-container">

    <%-- 1. 상태 코드를 한글로 변환하는 로직 --%>
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

    <div class="mail-header">
        <div class="header-top">
            <h3>
                <c:choose>
                    <c:when test="${mail.processType == 'TRADE'}"><span style="color:#28a745;">[거래]</span></c:when>
                    <c:when test="${mail.processType == 'RECRUIT'}"><span style="color:#ffc107; text-shadow: 1px 1px 1px #ddd;">[구인]</span></c:when>
                    <c:otherwise><span style="color:#6c757d;">[일반]</span></c:otherwise>
                </c:choose> 
                ${mail.title}
            </h3>
            
            <c:if test="${not empty mail.postId}">
                <a href="/post/${mail.postId}" class="btn btn-info" target="_blank">📄 관련 게시글</a>
            </c:if>
        </div>
        
        <div class="info-row">
            <span class="info-label">상태</span> 
            <span class="status-badge status-${mail.processStatus.name().contains('PENDING') ? 'PENDING' : (mail.processStatus.name().contains('ACCEPTED') or mail.processStatus.name().contains('COMPLETED') ? 'ACCEPTED' : (mail.processStatus.name().contains('REJECTED') ? 'REJECTED' : ''))}">
                ${statusKorean}
            </span>
        </div>
        <div class="info-row"><span class="info-label">보낸 사람</span> ${mail.sender}</div>
        <div class="info-row"><span class="info-label">받은 사람</span> ${mail.receiver}</div>
        <div class="info-row">
            <span class="info-label">날짜</span> 
            <fmt:parseDate value="${mail.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
        </div>
        <c:if test="${not empty mail.amount && mail.amount > 0}">
             <div class="info-row"><span class="info-label">금액</span> <strong><fmt:formatNumber value="${mail.amount}" type="currency"/></strong></div>
        </c:if>
    </div>

    <div class="mail-content">${mail.content}</div>

    <%-- ================= 액션 영역 (받는 사람인 경우에만 표시) ================= --%>
    <c:if test="${mail.receiverId == loginId}">
        
        <c:if test="${mail.processStatus != 'PROCESSED' && 
                      !mail.processStatus.name().contains('COMPLETED') && 
                      !mail.processStatus.name().contains('ACCEPTED') && 
                      !mail.processStatus.name().contains('REJECTED')}">

            <div class="action-box">
                <h4>⚡ 작업</h4>
                
                <form action="/mail/action" method="post">
                    <input type="hidden" name="mailId" value="${mail.id}">
                    <input type="hidden" name="postId" value="${mail.postId}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                    <%-- [추가] 공통 메시지 입력 영역 --%>
                    <div class="input-group">
                        <label>메시지 (선택 사항)</label>
                        <textarea name="content" placeholder="상대방에게 보낼 메시지를 입력하세요."></textarea>
                    </div>
                    
                    <%-- 구분선 --%>
                    <hr style="margin: 20px 0; border: 0; border-top: 1px solid #bcdbf3;">

                    <%-- 거래(Trade) 관련 액션 --%>
                    <c:if test="${mail.processType == 'TRADE'}">
                        
                        <c:if test="${mail.processStatus == 'TRADE_REQUEST_PENDING'}">
                            <p class="guide-text">구매자가 거래를 원합니다. 거래 시간과 장소를 제안해주세요.</p>
                            
                            <div class="input-group">
                                <label>거래 시간</label>
                                <input type="text" name="tradeTime" placeholder="예: 내일 오후 6시" required>
                            </div>
                            <div class="input-group">
                                <label>거래 장소</label>
                                <input type="text" name="tradeLocation" placeholder="예: 정문 시계탑 앞" required>
                            </div>
                            
                            <button type="submit" name="actionType" value="SET_CONDITIONS" class="btn btn-primary">📝 조건 보내기</button>
                        </c:if>

                        <c:if test="${mail.processStatus == 'TRADE_CONDITIONS_SET'}">
                            <p class="guide-text">판매자가 거래 조건을 제시했습니다. 이 조건으로 거래하시겠습니까?</p>
                            <button type="submit" name="actionType" value="ACCEPT_TRADE" class="btn btn-success">⭕ 수락하기</button>
                            <button type="submit" name="actionType" value="REJECT_TRADE" class="btn btn-danger">❌ 거절하기</button>
                        </c:if>

                        <c:if test="${mail.processStatus == 'TRADE_TRANSFER_PENDING'}">
                            <p class="guide-text">거래가 확정되었습니다. 약속된 금액을 송금해주세요.</p>
                            <p><strong>송금액:</strong> <fmt:formatNumber value="${mail.amount}" type="currency"/></p>
                            <button type="submit" name="actionType" value="TRANSFER" class="btn btn-primary">💸 송금하기</button>
                        </c:if>

                    </c:if>

                    <%-- 구인(Job) 관련 액션 --%>
                    <c:if test="${mail.processType == 'RECRUIT'}">
                        <c:if test="${mail.processStatus == 'RECRUIT_REQUEST_PENDING'}">
                            <p class="guide-text">이 지원자를 채용하시겠습니까?</p>
                            <button type="submit" name="actionType" value="ACCEPT_RECRUIT" class="btn btn-success">⭕ 채용하기</button>
                            <button type="submit" name="actionType" value="REJECT_RECRUIT" class="btn btn-danger">❌ 거절하기</button>
                        </c:if>
                    </c:if>

                </form>
            </div>
        </c:if>
    </c:if>

    <%-- ================= 상태 메시지 영역 ================= --%>
    <c:if test="${mail.processStatus == 'PROCESSED'}">
        <div style="margin-top: 30px; text-align: center; color: #888; font-weight: bold;">
            🆗 이미 답장(처리)하여 완료된 단계입니다.
        </div>
    </c:if>
    <c:if test="${mail.processStatus.name().contains('COMPLETED') || mail.processStatus.name().contains('ACCEPTED')}">
        <div style="margin-top: 30px; text-align: center; color: green; font-weight: bold;">
            ✅ 최종 처리가 완료된 건입니다.
        </div>
    </c:if>
    <c:if test="${mail.processStatus.name().contains('REJECTED')}">
        <div style="margin-top: 30px; text-align: center; color: red; font-weight: bold;">
            ❌ 거절되거나 취소된 건입니다.
        </div>
    </c:if>
    
    <%-- 보낸 사람 시점: 대기 중 메시지 --%>
    <c:if test="${mail.senderId == loginId && 
                  mail.processStatus != 'PROCESSED' &&
                  !mail.processStatus.name().contains('COMPLETED') && 
                  !mail.processStatus.name().contains('ACCEPTED') && 
                  !mail.processStatus.name().contains('REJECTED')}">
        <div style="margin-top: 30px; text-align: center; padding: 20px; background-color: #f9f9f9; border-radius: 8px; color: #666;">
            ⏳ <strong>상대방(${mail.receiver})의 응답을 기다리는 중입니다.</strong><br>
            상대방이 확인하고 조치를 취하면 상태가 변경됩니다.
        </div>
    </c:if>

    <a href="/mail" class="btn btn-list">목록으로 돌아가기</a>

</div>

<%-- [추가] 알림 스크립트: Controller에서 보낸 메시지나 에러가 있으면 alert를 띄움 --%>
<script>
    /* 성공 메시지 */
    <c:if test="${not empty message}">
        alert("${message}");
    </c:if>

    /* 에러 메시지 (예: 잔액 부족) */
    <c:if test="${not empty error}">
        alert("${error}");
    </c:if>
</script>

</body>
</html>