<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- 1. Spring Form Tag Library 추가 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <style>
        /* 오류 메시지 스타일 */
        .error-message { color: red; font-size: 0.9em; margin-left: 10px; }
    </style>
</head>
<body>

    <h1>회원가입</h1>
    
    <c:if test="${not empty msg}">
        <p style="color: blue;">${msg}</p>
    </c:if>

    <%-- 2. <form:form> 태그로 변경 및 modelAttribute 지정 --%>
    <%-- modelAttribute="member"는 Controller에서 @Valid Member member로 받은 객체 이름과 일치해야 합니다. --%>
    <form:form modelAttribute="member" method="post" action="/register">
        <table>
            <tr>
                <td>사용자 ID</td>
                <td>
                    <form:input path="userId" required="required" />
                    <%-- 3. 오류 출력 태그 추가: Member 도메인 객체의 "userId" 필드 오류를 출력합니다. --%>
                    <form:errors path="userId" cssClass="error-message"/>
                </td>
            </tr>
            <tr>
                <td>비밀번호</td>
                <td>
                    <form:password path="password" required="required" />
                    <%-- 비밀번호 필드 오류 출력 --%>
                    <form:errors path="password" cssClass="error-message"/>
                </td>
            </tr>
            <tr>
                <td>이름</td>
                <td>
                    <form:input path="userName" required="required" />
                    <%-- 이름 필드 오류 출력 --%>
                    <form:errors path="userName" cssClass="error-message"/>
                </td>
            </tr>
            <tr>
                <td>이메일</td>
                <td>
                    <form:input path="email" required="required" type="email" />
                    <%-- 이메일 필드 오류 출력 --%>
                    <form:errors path="email" cssClass="error-message"/>
                </td>
            </tr>
            <tr>
                <td colspan="2"><button type="submit">가입 완료</button></td>
            </tr>
        </table>
        
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form:form>

</body>
</html>