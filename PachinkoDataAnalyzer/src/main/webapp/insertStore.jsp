<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.DataSource" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>店舗登録</title>
</head>
<body>
    <h1>店舗登録</h1>
    <form action="insertStore" method="post">
        <!-- 店舗名入力 -->
        <label for="storeName">店舗名:</label>
        <input type="text" name="store_name" id="store_name" required>
        <br><br>

        <!-- 機種名入力 -->
        <label for="modelNames">機種名 (複数可、カンマ区切り):</label>
        <textarea name="model_name" id="model_name" rows="5" cols="50" 
                  placeholder="例: 機種A, 機種B, 機種C" required></textarea>
        <br><br>

        <!-- 登録ボタン -->
        <button type="submit">登録</button>
    </form>

    <!-- ホーム画面に戻るボタン -->
    <br><br>
    <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>
</body>
</html>
