<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
</head>
<body>

    <h1>회원가입</h1>
    
    <!-- 회원가입 성공 메시지 (리다이렉트 후 출력될 수 있음) -->
    <c:if test="${not empty msg}">
        <p style="color: blue;">${msg}</p>
    </c:if>

    <!-- 
        1. 회원가입 폼 (액션: /member/register)
        - 이 폼이 Controller의 @PostMapping("/register") 메서드를 실행시킵니다.
    -->
    <form method="post" action="/register">
        <table>
            <tr>
                <td>사용자 ID</td>
                <td><input type="text" name="userId" required></td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <td>이름</td>
                <td><input type="text" name="userName" required></td>
            </tr>
            <tr>
                <td>이메일</td>
                <td><input type="email" name="email" required></td>
            </tr>
            <tr>
                <td colspan="2"><button type="submit">가입 완료</button></td>
            </tr>
        </table>
        
        <!-- CSRF 토큰 (필수) -->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>

</body>
</html>
