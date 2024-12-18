<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${storeName} のデータを入力</title>
    <style>
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
            max-width: 600px;
            margin: 20px auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            font-weight: bold;
            margin-top: 15px;
        }
        select, input[type="date"], textarea, button {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }
        button {
            background-color: #007bff;
            color: white;
            cursor: pointer;
            margin-top: 20px;
        }
        button:hover {
            background-color: #0056b3;
        }
        .back-button {
            text-align: center;
            margin-top: 20px;
        }
        .back-button a {
            text-decoration: none;
            color: #007bff;
        }
        .back-button a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>${storeName} のデータを入力</h1>

    <form action="insertData" method="POST">
        <!-- 店舗名選択 -->
        <label for="store_name">店舗名</label>
        <select name="store_name" id="store_name" required>
            <option value="">店舗を選択</option>
            <c:forEach var="store" items="${storeName}">
                <option value="${store}" <c:if test="${store == storeName}">selected</c:if>>${store}</option>
            </c:forEach>
        </select>

        <!-- 機種名選択 -->
        <label for="model_name">機種名</label>
        <select name="model_name" id="model_name" required>
            <option value="">機種を選択</option>
            <c:forEach var="model" items="${modelNames}">
                <option value="${model}" <c:if test="${model == modelName}">selected</c:if>>${model}</option>
            </c:forEach>
        </select>

        <!-- 日付選択 -->
        <label for="data_date">データ日付</label>
        <input type="date" name="data_date" id="data_date" required>

        <!-- データ入力 -->
        <label for="pachinko_data">パチンコデータ</label>
        <textarea name="pachinko_data" id="pachinko_data" rows="10" required></textarea>

        <button type="submit">データを登録</button>
    </form>

    <!-- ホーム画面に戻るボタン -->
    <div class="back-button">
        <a href="index.jsp">ホーム画面に戻る</a>
    </div>
</body>
</html>
