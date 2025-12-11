<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>로그인</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 400px;
            margin-top: 100px;
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
    </style>
</head>
<body>

<div class="container d-flex justify-content-center">
    <div class="login-container w-100">
        <div class="card p-3">
            <div class="card-header text-primary">
                PoorDeal 로그인
            </div>
            <div class="card-body">

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger text-center" role="alert">
                        <c:choose>
                            <%-- 1. 핸들러에서 보낸 구체적인 메시지 (예: 정지된 계정) --%>
                            <c:when test="${not empty param.message}">
                                <i class="bi bi-exclamation-triangle-fill"></i> ${param.message}
                            </c:when>
                            <%-- 2. 메시지가 없는 경우 (기본: 아이디/비번 불일치) --%>
                            <c:otherwise>
                                아이디 또는 비밀번호가 일치하지 않습니다.
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <form method="post" action="/login">
                    <div class="mb-3">
                        <label for="userId" class="form-label fw-bold">아이디</label>
                        <input type="text" class="form-control" id="userId" name="userId" placeholder="아이디를 입력하세요" required autofocus>
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label fw-bold">비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">로그인</button>
                    </div>
                </form>

                <hr class="my-4">

                <div class="text-center">
                    <span class="text-muted small">아직 회원이 아니신가요?</span>
                    <a href="/register" class="btn btn-link text-decoration-none fw-bold">회원가입 하기</a>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>