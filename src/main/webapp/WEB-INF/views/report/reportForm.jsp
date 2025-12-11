<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>신고하기 - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .main-container {
            max-width: 600px;
            margin: 50px auto;
        }
        .report-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(220, 53, 69, 0.1);
            background: white;
            overflow: hidden;
        }
        .report-header {
            background-color: #fff;
            padding: 2rem 2rem 1rem 2rem;
            text-align: center;
        }
        .form-label {
            font-weight: 600;
            color: #495057;
            font-size: 0.95rem;
        }
        .input-group-text {
            background-color: #f8f9fa;
            border-right: none;
            color: #6c757d;
        }
        .form-control:focus {
            border-color: #e6b0b6;
            box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
        }
        .readonly-input {
            background-color: #e9ecef !important;
            color: #495057;
        }
    </style>
</head>
<body>

<div class="container main-container">
    <div class="report-card">
        
        <div class="report-header">
            <div class="mb-2">
                <i class="bi bi-shield-exclamation text-danger" style="font-size: 3rem;"></i>
            </div>
            <h3 class="fw-bold text-danger m-0">신고하기</h3>
            <p class="text-muted small mt-2">
                불쾌하거나 부적절한 내용을 발견하셨나요?<br>
                신고 사유를 작성해주시면 신속하게 처리하겠습니다.
            </p>
        </div>

        <div class="p-4">
            
            <div class="alert alert-warning d-flex align-items-center mb-4" role="alert">
                <i class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2"></i>
                <div class="small">
                    허위 신고 시, 신고자에게 이용 제한 등의 불이익이 있을 수 있습니다.
                </div>
            </div>

            <form action="/report/submit" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <input type="hidden" name="suspectId" value="${reportRequest.suspectId}">
                <input type="hidden" name="postId" value="${reportRequest.postId}">

                <div class="mb-4">
                    <label class="form-label">신고 대상</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person-x-fill text-danger"></i></span>
                        <input type="text" class="form-control readonly-input" value="${suspectName}" readonly disabled>
                        <span class="input-group-text readonly-input"><i class="bi bi-lock-fill"></i></span>
                    </div>
                </div>

                <c:if test="${not empty reportRequest.postId}">
                    <div class="mb-4">
                        <label class="form-label">관련 게시글 ID</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-hash"></i></span>
                            <input type="text" class="form-control readonly-input" value="${reportRequest.postId}" readonly disabled>
                            <span class="input-group-text readonly-input"><i class="bi bi-lock-fill"></i></span>
                        </div>
                    </div>
                </c:if>

                <div class="mb-4">
                    <label for="reason" class="form-label">신고 사유 <span class="text-danger">*</span></label>
                    <textarea class="form-control" id="reason" name="reason" rows="6" 
                              placeholder="신고 사유를 구체적으로 입력해주세요. (예: 욕설/비하 발언, 사기 의심 등)" required style="resize: none;"></textarea>
                </div>

                <hr class="my-4">

                <div class="d-flex justify-content-end gap-2">
                    <button type="button" class="btn btn-light border px-4" onclick="history.back()">
                        취소
                    </button>
                    <button type="submit" class="btn btn-danger fw-bold shadow-sm px-4">
                        <i class="bi bi-send-fill me-1"></i>신고 접수
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>