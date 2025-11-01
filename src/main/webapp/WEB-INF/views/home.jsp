<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PoorDeal: 믿을 수 있는 중고거래</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        header { border-bottom: 2px solid #ddd; padding-bottom: 10px; margin-bottom: 20px; }
        .user-info a { margin-left: 15px; text-decoration: none; color: #007bff; }
        .product-grid { display: flex; flex-wrap: wrap; gap: 20px; }
        .product-card { border: 1px solid #ccc; padding: 15px; width: 200px; box-shadow: 2px 2px 5px rgba(0,0,0,0.1); }
        .product-card img { width: 100%; height: auto; margin-bottom: 10px; }
        .price { color: #d9534f; font-weight: bold; }
    </style>
</head>
<body>

    <header>
        <h1>PoorDeal 🛒</h1>
        
        <div class="user-info">
            <sec:authorize access="isAuthenticated()">
                환영합니다, 
                <b><sec:authentication property="principal.username" /></b>님!
                
                <form method="post" action="/logout" style="display: inline;">
                    <button type="submit">로그아웃</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </sec:authorize>
            
            <sec:authorize access="isAnonymous()">
                <a href="/login">로그인</a>
                <a href="/register">회원가입</a>
            </sec:authorize>
        </div>
    </header>

    <h2>오늘의 인기 더미 상품 ✨</h2>

    <div class="product-grid">
        
        <div class="product-card">
            <h3>최신형 노트북 (급처)</h3>
            <p>상태: A+급, 풀박스</p>
            <p class="price">750,000 원</p>
            <button>자세히 보기</button>
        </div>

        <div class="product-card">
            <h3>접이식 자전거</h3>
            <p>지역: 서울 강남</p>
            <p class="price">150,000 원</p>
            <button>자세히 보기</button>
        </div>

        <div class="product-card">
            <h3>빈티지 필름 카메라</h3>
            <p>희귀 매물, 작동 확인 완료</p>
            <p class="price">320,000 원</p>
            <button>자세히 보기</button>
        </div>
        
        <sec:authorize access="isAuthenticated()">
            <div class="product-card" style="text-align: center; border-style: dashed; display: flex; align-items: center; justify-content: center;">
                <a href="/board/write" style="text-decoration: none; font-size: 1.2em; color: #28a745;">+ 상품 등록하기</a>
            </div>
        </sec:authorize>
    </div>

</body>
</html>