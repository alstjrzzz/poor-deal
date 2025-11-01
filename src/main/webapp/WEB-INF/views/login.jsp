<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
</head>
<body>

    <h1>사용자 로그인</h1>

    <!-- 1. 로그인 실패 메시지 -->
    <c:if test="${param.error != null}">
        <p style="color: red;">아이디 또는 비밀번호가 일치하지 않습니다.</p>
    </c:if>

    <!-- 2. Spring Security 로그인 폼 (액션: /member/login) -->
    <form method="post" action="/login">
        <table>
            <tr>
                <td>아이디 (userId)</td>
                <td><input type="text" name="userId" required></td>
            </tr>
            <tr>
                <td>비밀번호 (password)</td>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <td colspan="2"><button type="submit">로그인</button></td>
            </tr>
        </table>
        
        <!-- CSRF 토큰 (필수) -->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>

    <hr>
    
    <!-- 💡 3. 회원가입 페이지 이동 버튼 추가 -->
    <form method="get" action="/register">
        <button type="submit">회원가입 하기</button>
    </form>

</body>
</html>
