<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>회원가입</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .register-container {
            max-width: 500px; /* 입력 필드가 많으므로 로그인보다 조금 더 넓게 */
            margin-top: 50px;
            margin-bottom: 50px;
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            background-color: transparent;
            border-bottom: none;
            font-weight: bold;
            font-size: 1.5rem;
            text-align: center;
            padding-top: 30px;
            padding-bottom: 20px;
        }
        .btn-primary {
            background-color: #0d6efd;
            border: none;
            padding: 10px;
            font-size: 1.1rem;
        }
        .btn-primary:hover {
            background-color: #0b5ed7;
        }
        /* 유효성 검사 에러 메시지 스타일 */
        .field-error {
            color: #dc3545; /* Bootstrap Danger Color */
            font-size: 0.875rem;
            margin-top: 0.25rem;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="container d-flex justify-content-center">
    <div class="register-container w-100">
        <div class="card p-3">
            <div class="card-header text-primary">
                PoorDeal 회원가입
            </div>
            <div class="card-body">

                <%-- 서버에서 보내준 일반 메시지 (예: 로그아웃 됨 등) --%>
                <c:if test="${not empty msg}">
                    <div class="alert alert-info text-center">
                        ${msg}
                    </div>
                </c:if>

                <%-- Spring Form 시작 --%>
                <form:form modelAttribute="member" method="post" action="/register">
                    
                    <div class="mb-3">
                        <label for="userId" class="form-label fw-bold">아이디</label>
                        <form:input path="userId" class="form-control" placeholder="아이디를 입력하세요" />
                        <form:errors path="userId" cssClass="field-error" />
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label fw-bold">비밀번호</label>
                        <form:password path="password" class="form-control" placeholder="비밀번호 (8자 이상, 특수문자 포함)" />
                        <form:errors path="password" cssClass="field-error" />
                    </div>

                    <div class="mb-3">
                        <label for="userName" class="form-label fw-bold">이름</label>
                        <form:input path="userName" class="form-control" placeholder="이름을 입력하세요" />
                        <form:errors path="userName" cssClass="field-error" />
                    </div>

                    <div class="mb-4">
                        <label for="email" class="form-label fw-bold">이메일</label>
                        <form:input path="email" type="email" class="form-control" placeholder="example@email.com" />
                        <form:errors path="email" cssClass="field-error" />
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">가입 완료</button>
                    </div>

                </form:form>
                <%-- Spring Form 끝 --%>

                <hr class="my-4">

                <div class="text-center">
                    <span class="text-muted small">이미 계정이 있으신가요?</span>
                    <a href="/login" class="btn btn-link text-decoration-none fw-bold">로그인 하러가기</a>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>