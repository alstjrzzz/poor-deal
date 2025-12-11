<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 작성 - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .main-container {
            max-width: 800px;
            margin: 50px auto;
        }
        .write-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            background: white;
        }
        .form-label {
            font-weight: 600;
            color: #495057;
        }
        .input-group-text {
            background-color: #f8f9fa;
            border-right: none;
            color: #6c757d;
        }
        .form-control:focus, .form-select:focus {
            border-color: #86b7fe;
            box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
        }
        .input-group .form-control, .input-group .form-select {
            border-left: none;
        }
    </style>
</head>
<body>

<div class="container main-container">
    <div class="write-card p-4 p-md-5">
        
        <div class="d-flex align-items-center mb-4 border-bottom pb-3">
            <i class="bi bi-pencil-fill fs-3 text-primary me-2"></i>
            <h3 class="fw-bold m-0">게시글 작성</h3>
        </div>

        <form action="/post" method="post" enctype="multipart/form-data">
        
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"> 
            
            <div class="mb-4">
                <label for="title" class="form-label">제목</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-type-h1"></i></span>
                    <input type="text" class="form-control" id="title" name="title" required placeholder="제목을 입력하세요">
                </div>
            </div>

            <div class="mb-4">
                <label for="type" class="form-label">카테고리</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-tags"></i></span>
                    <select class="form-select" id="type" name="type" onchange="toggleFields()">
                        <option value="FREE">자유게시판 (FREE)</option>
                        <option value="TRADE">중고거래 (TRADE)</option>
                        <option value="JOB">구인구직 (JOB)</option>
                    </select>
                </div>
                <div class="form-text ms-1 mt-2">
                    <i class="bi bi-info-circle me-1"></i>작성 후 카테고리는 변경할 수 없습니다.
                </div>
            </div>

            <div class="mb-4" id="priceDiv" style="display:none;">
                <label for="price" class="form-label">가격</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-currency-exchange"></i></span>
                    <input type="number" class="form-control" id="price" name="price" value="0" placeholder="판매 가격을 입력하세요">
                    <span class="input-group-text bg-white border-start-0">원</span>
                </div>
            </div>

            <div class="mb-4" id="quotaDiv" style="display:none;">
                <label for="hiringQuota" class="form-label">모집 인원</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-people"></i></span>
                    <input type="number" class="form-control" id="hiringQuota" name="hiringQuota" value="0" placeholder="모집할 인원을 입력하세요">
                    <span class="input-group-text bg-white border-start-0">명</span>
                </div>
            </div>

            <div class="mb-4">
                <label for="image" class="form-label">이미지 첨부</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-image"></i></span>
                    <input type="file" class="form-control" id="image" name="image" accept="image/*">
                </div>
                <div class="form-text ms-1 text-muted">
                    이미지는 선택 사항입니다.
                </div>
            </div>

            <div class="mb-5">
                <label for="content" class="form-label">내용</label>
                <textarea class="form-control" id="content" name="content" rows="12" required placeholder="내용을 자유롭게 작성해주세요." style="resize: none;"></textarea>
            </div>

            <hr class="my-4">

            <div class="d-flex justify-content-end gap-2">
                <a href="/" class="btn btn-secondary px-4">취소</a>
                <button type="submit" class="btn btn-primary px-4 fw-bold">등록</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // 카테고리 선택에 따라 입력 필드 보이기/숨기기
    function toggleFields() {
        const type = document.getElementById("type").value;
        const priceDiv = document.getElementById("priceDiv");
        const quotaDiv = document.getElementById("quotaDiv");

        // 초기화 (모두 숨김)
        priceDiv.style.display = "none";
        quotaDiv.style.display = "none";

        // 선택값에 따라 보이기
        if (type === "TRADE") {
            priceDiv.style.display = "block";
            // UX 향상을 위해 가격 필드에 포커스
            document.getElementById("price").focus();
        } else if (type === "JOB") {
            quotaDiv.style.display = "block";
            // UX 향상을 위해 인원 필드에 포커스
            document.getElementById("hiringQuota").focus();
        }
    }
</script>
</body>
</html>