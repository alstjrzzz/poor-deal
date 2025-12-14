<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 수정 - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; }
        .main-container { max-width: 800px; margin: 50px auto; }
        .edit-card { border: none; border-radius: 15px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); background: white; }
        .form-label { font-weight: 600; color: #495057; }
        .current-img-box { position: relative; display: inline-block; border: 1px solid #dee2e6; padding: 5px; border-radius: 8px; background: #fff; }
        .input-group-text { background-color: #f8f9fa; border-right: none; }
        .form-control:focus, .form-select:focus { border-color: #86b7fe; box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25); }
        .input-group .form-control { border-left: none; }
    </style>
</head>
<body>

<div class="container main-container">
    <div class="edit-card p-4 p-md-5">
        
        <div class="d-flex align-items-center mb-4 border-bottom pb-3">
            <i class="bi bi-pencil-square fs-3 text-primary me-2"></i>
            <h3 class="fw-bold m-0">게시글 수정</h3>
        </div>

        <form action="/post/${post.id}/edit" method="post" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            
            <div class="mb-4">
                <label for="title" class="form-label">제목</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-type-h1"></i></span>
                    <input type="text" class="form-control" id="title" name="title" value="${post.title}" required placeholder="제목을 입력하세요">
                </div>
            </div>

            <div class="mb-4">
                <label for="type" class="form-label">카테고리 <small class="text-danger fw-normal ms-1">*수정 불가</small></label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-tags"></i></span>
                    <select class="form-select bg-light text-secondary" id="type" disabled style="cursor: not-allowed;">
                        <option value="FREE" ${post.type == 'FREE' ? 'selected' : ''}>자유게시판</option>
                        <option value="TRADE" ${post.type == 'TRADE' ? 'selected' : ''}>중고거래</option>
                        <option value="JOB" ${post.type == 'JOB' ? 'selected' : ''}>구인구직</option>
                    </select>
                </div>
                <input type="hidden" name="type" value="${post.type}">
            </div>

            <div class="mb-4" id="priceField" style="display: none;">
                <label for="price" class="form-label">가격</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-currency-exchange"></i></span>
                    <input type="number" class="form-control" id="price" name="price" value="${post.price}" placeholder="가격을 입력하세요">
                    <span class="input-group-text bg-white border-start-0">원</span>
                </div>
            </div>

            <div class="mb-4" id="quotaField" style="display: none;">
                <label for="hiringQuota" class="form-label">모집 인원 <small class="text-danger fw-normal ms-1">*수정 불가</small></label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-people"></i></span>
                    <input type="number" class="form-control bg-light text-secondary" id="hiringQuota" name="hiringQuota" 
                           value="${post.hiringQuota}" readonly style="cursor: not-allowed;">
                    <span class="input-group-text bg-light text-secondary border-start-0">명</span>
                </div>
            </div>

            <div class="mb-4">
                <label for="content" class="form-label">내용</label>
                <textarea class="form-control" id="content" name="content" rows="12" required placeholder="내용을 입력하세요" style="resize: none;">${post.content}</textarea>
            </div>

            <div class="mb-5">
                <label for="image" class="form-label">이미지 첨부</label>
                <div class="card bg-light border-0 p-3">
                    <c:if test="${not empty post.image}">
                        <div class="mb-3">
                            <span class="badge bg-secondary mb-2">현재 등록된 이미지</span>
                            <div class="d-block">
                                <div class="current-img-box shadow-sm">
                                    <img src="${post.image}?v=<%=System.currentTimeMillis()%>" alt="기존 이미지" style="height: 120px; width: auto; object-fit: contain;" class="rounded">
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-image"></i></span>
                        <input class="form-control" type="file" id="image" name="image" accept="image/*">
                    </div>
                    <div class="form-text mt-2 ms-1 text-muted">
                        <i class="bi bi-info-circle me-1"></i>새로운 이미지를 선택하면 기존 이미지는 삭제되고 대체됩니다. (선택하지 않으면 유지)
                    </div>
                </div>
            </div>

            <hr class="my-4">

            <div class="d-flex justify-content-end gap-2">
                <a href="/post/${post.id}" class="btn btn-secondary px-4">취소</a>
                <button type="submit" class="btn btn-primary px-4 fw-bold">수정 완료</button>
            </div>
        </form>
    </div>
</div>

<div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 11">
    <c:if test="${not empty error}">
        <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body fw-semibold"><i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function toggleFields() {
        const type = document.getElementById('type').value;
        const priceField = document.getElementById('priceField');
        const quotaField = document.getElementById('quotaField');

        priceField.style.display = 'none';
        quotaField.style.display = 'none';

        if (type === 'TRADE') {
            priceField.style.display = 'block';
        } else if (type === 'JOB') {
            quotaField.style.display = 'block';
        }
    }

    window.onload = function() {
        toggleFields();
        // Toast Alert
        <c:if test="${not empty error}">
            new bootstrap.Toast(document.getElementById('errorToast'), { delay: 5000 }).show();
        </c:if>
    };
</script>
</body>
</html>