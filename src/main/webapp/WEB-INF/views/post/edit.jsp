<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5 mb-5" style="max-width: 800px;">
    <h2 class="mb-4">게시글 수정</h2>

    <form action="/post/${post.id}/edit" method="post" enctype="multipart/form-data">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        
        <div class="mb-3">
            <label for="title" class="form-label fw-bold">제목</label>
            <input type="text" class="form-control" id="title" name="title" value="${post.title}" required>
        </div>

        <div class="mb-3">
            <label for="type" class="form-label fw-bold">카테고리</label>
            
            <select class="form-select bg-light" id="type" disabled>
                <option value="FREE" ${post.type == 'FREE' ? 'selected' : ''}>자유게시판</option>
                <option value="TRADE" ${post.type == 'TRADE' ? 'selected' : ''}>중고거래</option>
                <option value="JOB" ${post.type == 'JOB' ? 'selected' : ''}>구인구직</option>
            </select>

            <input type="hidden" name="type" value="${post.type}">
            
            <div class="form-text">※ 카테고리는 수정할 수 없습니다.</div>
        </div>
        <div class="mb-3" id="priceField" style="display: none;">
            <label for="price" class="form-label fw-bold">가격 (원)</label>
            <input type="number" class="form-control" id="price" name="price" value="${post.price}" placeholder="가격을 입력하세요">
        </div>

        <div class="mb-3" id="quotaField" style="display: none;">
            <label for="hiringQuota" class="form-label fw-bold">모집 인원 (명)</label>
            <input type="number" class="form-control bg-light" id="hiringQuota" name="hiringQuota" 
                   value="${post.hiringQuota}" readonly placeholder="모집 인원은 수정할 수 없습니다">
            <div class="form-text">※ 모집 인원은 수정할 수 없습니다.</div>
        </div>

        <div class="mb-3">
            <label for="content" class="form-label fw-bold">내용</label>
            <textarea class="form-control" id="content" name="content" rows="10" required>${post.content}</textarea>
        </div>

        <div class="mb-3">
            <label for="image" class="form-label fw-bold">이미지 첨부</label>
            
            <c:if test="${not empty post.image}">
                <div class="mb-2 p-2 border rounded bg-light">
                    <p class="mb-1 text-muted small">현재 등록된 이미지:</p>
                    <img src="${post.image}?v=<%=System.currentTimeMillis()%>" alt="기존 이미지" style="height: 100px; width: auto;" class="rounded">
                </div>
            </c:if>
            
            <input class="form-control" type="file" id="image" name="image" accept="image/*">
            <div class="form-text">새로운 이미지를 선택하면 기존 이미지는 대체됩니다. (선택하지 않으면 기존 이미지 유지)</div>
        </div>

        <hr>

        <div class="d-flex justify-content-between">
            <a href="/post/${post.id}" class="btn btn-secondary">취소</a>
            <button type="submit" class="btn btn-primary">수정 완료</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function toggleFields() {
        // ID가 type인 요소(select)의 값을 가져옴 (disabled여도 값 읽기는 가능)
        const type = document.getElementById('type').value;
        const priceField = document.getElementById('priceField');
        const quotaField = document.getElementById('quotaField');

        // 초기화
        priceField.style.display = 'none';
        quotaField.style.display = 'none';

        // 선택에 따른 노출
        if (type === 'TRADE') {
            priceField.style.display = 'block';
        } else if (type === 'JOB') {
            quotaField.style.display = 'block';
        }
    }

    // 페이지 로드 시 현재 타입에 맞춰 필드 노출 상태 설정
    window.onload = function() {
        toggleFields();
    };
</script>
</body>
</html>