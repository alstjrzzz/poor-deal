<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5" style="max-width: 800px;">
    <h2 class="mb-4">게시글 작성</h2>
    
    <form action="/post" method="post" enctype="multipart/form-data">
    
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"> 
        
        <div class="mb-3">
            <label for="title" class="form-label">제목</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>

        <div class="mb-3">
            <label for="type" class="form-label">카테고리</label>
            <select class="form-select" id="type" name="type" onchange="toggleFields()">
                <option value="FREE">자유게시판 (FREE)</option>
                <option value="TRADE">중고거래 (TRADE)</option>
                <option value="JOB">구인구직 (JOB)</option>
            </select>
        </div>

        <div class="mb-3" id="priceDiv" style="display:none;">
            <label for="price" class="form-label">가격 (원)</label>
            <input type="number" class="form-control" id="price" name="price" value="0">
        </div>

        <div class="mb-3" id="quotaDiv" style="display:none;">
            <label for="hiringQuota" class="form-label">모집 인원 (명)</label>
            <input type="number" class="form-control" id="hiringQuota" name="hiringQuota" value="0">
        </div>

        <div class="mb-3">
            <label for="image" class="form-label">이미지 첨부</label>
            <input type="file" class="form-control" id="image" name="image" accept="image/*">
        </div>

        <div class="mb-3">
            <label for="content" class="form-label">내용</label>
            <textarea class="form-control" id="content" name="content" rows="10" required></textarea>
        </div>

        <div class="d-flex justify-content-end">
            <a href="/" class="btn btn-secondary me-2">취소</a>
            <button type="submit" class="btn btn-primary">등록</button>
        </div>
    </form>
</div>

<script>
    // 카테고리 선택에 따라 입력 필드 보이기/숨기기
    function toggleFields() {
        const type = document.getElementById("type").value;
        const priceDiv = document.getElementById("priceDiv");
        const quotaDiv = document.getElementById("quotaDiv");

        // 초기화
        priceDiv.style.display = "none";
        quotaDiv.style.display = "none";

        if (type === "TRADE") {
            priceDiv.style.display = "block";
        } else if (type === "JOB") {
            quotaDiv.style.display = "block";
        }
    }
</script>
</body>
</html>