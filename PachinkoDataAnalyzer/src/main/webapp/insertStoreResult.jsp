<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登録結果</title>
    <style>
        /* 全体のスタイル */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .message {
            max-width: 500px;
            margin: 20px auto;
            padding: 15px;
            border-radius: 5px;
            text-align: center;
            font-size: 18px;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
        }
        .back-link a {
            text-decoration: none;
            color: #007bff;
            font-size: 16px;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
    <script>
        // JavaScript: ページロード時のアニメーション
        document.addEventListener("DOMContentLoaded", function () {
            const messages = document.querySelectorAll(".message");
            messages.forEach(message => {
                message.style.opacity = "0";
                setTimeout(() => {
                    message.style.transition = "opacity 1s";
                    message.style.opacity = "1";
                }, 100);
            });
        });
    </script>
</head>
<body>
    <h1>登録結果</h1>

    <!-- エラーメッセージ表示 -->
    <c:if test="${not empty errorMessage}">
        <div class="message error">${errorMessage}</div>
    </c:if>

    <!-- 成功メッセージ表示 -->
    <c:if test="${not empty successMessage}">
        <div class="message success">${successMessage}</div>
    </c:if>

    <div class="back-link">
        <a href="insertStore.jsp">店舗・機種登録画面に戻る</a>
    </div>
</body>
</html>
