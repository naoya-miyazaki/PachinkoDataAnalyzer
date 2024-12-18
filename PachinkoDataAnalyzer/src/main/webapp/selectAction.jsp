<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>操作選択</title>
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
            margin-bottom: 30px;
        }
        form {
            max-width: 400px;
            margin: 10px auto;
            text-align: center;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
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
    <h1>${storeName} のデータ操作を選択してください</h1>

    <!-- データ入力ボタン -->
    <form action="insertData" method="get">
        <input type="hidden" name="store_name" value="${storeName}">
        <input type="submit" value="データ入力">
    </form>

    <!-- データ閲覧ボタン -->
    <form action="viewData" method="get">
        <input type="hidden" name="store_name" value="${storeName}">
        <input type="submit" value="データ閲覧">
    </form>

    <!-- ホーム画面に戻るボタン -->
    <div class="back-button">
        <a href="index.jsp">ホーム画面に戻る</a>
    </div>
</body>
</html>
