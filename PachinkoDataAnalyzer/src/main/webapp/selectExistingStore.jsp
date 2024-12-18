<%@ page contentType="text/html; charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登録店舗の選択</title>
    <style>
        /* 全体のスタイル */
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            color: #343a40;
        }
        form {
            max-width: 400px;
            margin: 20px auto;
            padding: 20px;
            background-color: #ffffff;
            border: 1px solid #ced4da;
            border-radius: 8px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #495057;
        }
        select {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
            border: 1px solid #ced4da;
            border-radius: 4px;
        }
        input[type="submit"] {
            display: block;
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .message {
            max-width: 400px;
            margin: 10px auto;
            padding: 10px;
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            text-align: center;
        }
        .back-button {
            display: block;
            text-align: center;
            margin-top: 20px;
        }
        .back-button a {
            text-decoration: none;
            color: #007bff;
            font-size: 16px;
        }
        .back-button a:hover {
            text-decoration: underline;
        }
    </style>
    
</head>
<body>
    <h1>登録されている店舗を選択してください</h1>

    <!-- メッセージ表示 -->
    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>

    <!-- 店舗選択フォーム -->
    <form action="storeAction" method="get">
        <label for="store_name">店舗名:</label>
        <select name="store_name" id="store_name" required>
            <c:forEach var="store" items="${storeNames}">
                <option value="${store}">${store}</option>
            </c:forEach>
        </select>
        <input type="submit" value="選択">
    </form>

    <!-- ホーム画面に戻るボタン -->
    <div class="back-button">
        <a href="index.jsp">ホーム画面に戻る</a>
    </div>
</body>
</html>
