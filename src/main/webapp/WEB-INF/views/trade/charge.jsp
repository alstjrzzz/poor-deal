<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>포인트 충전 - PoorDeal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .charge-container {
            max-width: 480px;
            margin: 60px auto;
        }
        .charge-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
            background: white;
            overflow: hidden;
        }
        .charge-header {
            background-color: #fff;
            padding: 2rem 2rem 1rem 2rem;
            text-align: center;
        }
        .btn-amount {
            border: 1px solid #dee2e6;
            background-color: #fff;
            color: #495057;
            padding: 12px;
            border-radius: 10px;
            font-weight: 500;
            transition: all 0.2s;
        }
        .btn-amount:hover {
            background-color: #f8f9fa;
            transform: translateY(-2px);
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }
        /* 선택된 버튼 스타일 */
        .btn-amount.active {
            border-color: #0d6efd;
            background-color: #e7f1ff;
            color: #0d6efd;
            font-weight: bold;
        }
        .form-control:focus {
            box-shadow: none;
            border-color: #0d6efd;
        }
        .input-group-text {
            background-color: #f8f9fa;
            border-right: none;
        }
        .form-control {
            border-left: none;
            text-align: right;
            font-weight: bold;
            font-size: 1.2rem;
        }
        /* 크롬 등에서 숫자 입력 화살표 제거 */
        input[type=number]::-webkit-inner-spin-button, 
        input[type=number]::-webkit-outer-spin-button { 
            -webkit-appearance: none; 
            margin: 0; 
        }
    </style>
</head>
<body>

<div class="container charge-container">
    <div class="charge-card">
        
        <div class="charge-header">
            <div class="mb-3">
                <div class="d-inline-flex align-items-center justify-content-center bg-primary bg-opacity-10 text-primary rounded-circle" style="width: 60px; height: 60px;">
                    <i class="bi bi-wallet2 fs-2"></i>
                </div>
            </div>
            <h3 class="fw-bold m-0">포인트 충전</h3>
            <p class="text-muted small mt-2 mb-0">서비스 이용을 위한 포인트를 충전합니다.</p>
        </div>

        <div class="p-4">
            <form action="/trade/charge" method="post" id="chargeForm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="row g-2 mb-4">
                    <div class="col-6">
                        <button type="button" class="btn btn-amount w-100" onclick="selectAmount(10000)">10,000원</button>
                    </div>
                    <div class="col-6">
                        <button type="button" class="btn btn-amount w-100" onclick="selectAmount(30000)">30,000원</button>
                    </div>
                    <div class="col-6">
                        <button type="button" class="btn btn-amount w-100" onclick="selectAmount(50000)">50,000원</button>
                    </div>
                    <div class="col-6">
                        <button type="button" class="btn btn-amount w-100" onclick="selectAmount(100000)">100,000원</button>
                    </div>
                </div>

                <div class="mb-4">
                    <label for="amountInput" class="form-label text-muted small fw-bold">충전 금액 직접 입력</label>
                    <div class="input-group input-group-lg border rounded-3 overflow-hidden">
                        <span class="input-group-text border-0 text-muted">₩</span>
                        <input type="number" id="amountInput" name="amount" class="form-control border-0" 
                               placeholder="0" min="1000" step="1000" required>
                        <span class="input-group-text border-0 bg-white">원</span>
                    </div>
                    <div class="form-text text-end mt-2" id="msgArea">
                        최소 충전 금액은 1,000원입니다.
                    </div>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary btn-lg fw-bold shadow-sm">
                        <i class="bi bi-lightning-charge-fill me-1"></i>충전하기
                    </button>
                    <a href="/" class="btn btn-light btn-lg text-secondary fw-bold">
                        취소
                    </a>
                </div>
            </form>
        </div>
    </div>
    
    <div class="text-center mt-4 text-muted small">
        <i class="bi bi-shield-check me-1"></i>안전한 결제를 위해 보안 연결을 사용합니다.
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // 금액 버튼 클릭 시 input에 값 입력 및 스타일 변경 함수
    function selectAmount(value) {
        // 값 입력
        const input = document.getElementById('amountInput');
        input.value = value;

        // 버튼 스타일 활성화 (선택된 것만 파란색으로)
        const buttons = document.querySelectorAll('.btn-amount');
        buttons.forEach(btn => {
            // 버튼 텍스트에서 콤마와 '원'을 제거하고 숫자만 비교
            const btnValue = parseInt(btn.innerText.replace(/[^0-9]/g, ''));
            
            if (btnValue === value) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });
    }

    // 직접 입력 시 버튼 활성화 상태 해제
    document.getElementById('amountInput').addEventListener('input', function() {
        const buttons = document.querySelectorAll('.btn-amount');
        buttons.forEach(btn => btn.classList.remove('active'));
    });
    
    // 폼 제출 전 유효성 검사
    document.getElementById('chargeForm').addEventListener('submit', function(e) {
        const amount = document.getElementById('amountInput').value;
        if(amount <= 0) {
            alert("0원보다 큰 금액을 입력해주세요.");
            e.preventDefault();
        }
    });
</script>

</body>
</html>