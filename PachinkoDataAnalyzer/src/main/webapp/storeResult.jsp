<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>店舗登録結果</title>
    <style>
        .message {
            margin-top: 20px;
            padding: 10px;
            border: 1px solid #ccc;
            font-size: 16px;
        }
        .success {
            color: green;
            background-color: #eaffea;
            border-color: #a5d6a5;
        }
        .error {
            color: red;
            background-color: #ffeaea;
            border-color: #d6a5a5;
        }
    </style>
</head>
<body>
    <h1>店舗登録結果</h1>
    <div class="message ${message.startsWith('エラー') ? 'error' : 'success'}">
        ${message}
    </div>
    <a href="index.jsp">戻る</a>
</body>
</html>
