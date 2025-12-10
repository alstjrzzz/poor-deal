<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>포인트 충전</title>
    <style>
        body { background-color: #f5f6f8; font-family: 'Malgun Gothic', sans-serif; }
        
        .charge-container { 
            background: white;
            border: 1px solid #ddd; 
            padding: 40px; 
            width: 500px; 
            margin: 50px auto; 
            border-radius: 8px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.05); 
            text-align: center;
        }

        .charge-header { margin-bottom: 30px; }
        .charge-header h2 { margin: 0; color: #333; font-size: 24px; }
        .charge-header p { color: #666; margin-top: 10px; font-size: 14px; }

        /* 금액 선택 버튼 그리드 */
        .amount-grid { 
            display: grid; 
            grid-template-columns: 1fr 1fr; 
            gap: 10px; 
            margin-bottom: 20px; 
        }
        
        .btn-amount {
            padding: 12px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            color: #555;
            transition: 0.2s;
        }
        .btn-amount:hover { background-color: #f1f3f5; border-color: #c5c9cd; }
        .btn-amount.active { background-color: #e3f2fd; border-color: #2196f3; color: #1976d2; font-weight: bold; }

        /* 입력 폼 스타일 */
        .input-group { margin-bottom: 25px; text-align: left; }
        .input-group label { display: block; font-weight: bold; margin-bottom: 8px; color: #333; }
        .input-group input { 
            width: 100%; 
            padding: 12px; 
            border: 1px solid #ccc; 
            border-radius: 6px; 
            box-sizing: border-box; 
            font-size: 16px;
            text-align: right;
        }
        .input-group input:focus { outline: none; border-color: #2196f3; }

        /* 하단 버튼 */
        .btn-action { width: 100%; padding: 14px; font-size: 16px; border: none; border-radius: 6px; cursor: pointer; font-weight: bold; margin-bottom: 10px; }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-primary:hover { background-color: #0069d9; }
        .btn-secondary { background-color: #e9ecef; color: #495057; text-decoration: none; display: inline-block; box-sizing: border-box;}
        .btn-secondary:hover { background-color: #dee2e6; }

    </style>
</head>
<body>

<div class="charge-container">
    
    <div class="charge-header">
        <h2>💰 포인트 충전</h2>
        <p>서비스 이용을 위한 포인트를 충전합니다.</p>
    </div>

    <form action="/trade/charge" method="post" id="chargeForm">
        <%-- CSRF 토큰 (Spring Security 사용 시 필수) --%>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <div class="amount-grid">
            <button type="button" class="btn-amount" onclick="selectAmount(10000)">10,000원</button>
            <button type="button" class="btn-amount" onclick="selectAmount(30000)">30,000원</button>
            <button type="button" class="btn-amount" onclick="selectAmount(50000)">50,000원</button>
            <button type="button" class="btn-amount" onclick="selectAmount(100000)">100,000원</button>
        </div>

        <div class="input-group">
            <label for="amountInput">충전 금액</label>
            <%-- name="amount"가 PointChargeRequest의 amount 필드와 매핑됩니다 --%>
            <input type="number" id="amountInput" name="amount" placeholder="충전할 금액을 입력하세요" min="1000" step="1000" required>
        </div>

        <button type="submit" class="btn-action btn-primary">충전하기</button>
        <a href="/" class="btn-action btn-secondary">취소</a>
    </form>

</div>

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