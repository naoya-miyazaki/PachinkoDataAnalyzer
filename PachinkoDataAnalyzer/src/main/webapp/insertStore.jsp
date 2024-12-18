<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.DataSource" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>店舗登録</title>
    <style>
        /* CSS: 全体のスタイル設定 */
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        form {
            max-width: 500px;
            margin: 0 auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        button:hover {
            background-color: #0056b3;
        }
        a button {
            background-color: #6c757d;
        }
        a button:hover {
            background-color: #5a6268;
        }
    </style>
    <script>
        // JavaScript: 入力チェックと確認ダイアログ
        function validateForm() {
            const storeName = document.getElementById("store_name").value.trim();
            if (storeName === "") {
                alert("店舗名を入力してください。");
                return false;
            }
            return confirm("この内容で登録してもよろしいですか？");
        }
    </script>
</head>
<body>
    <h1>店舗登録</h1>
    <form action="insertStore" method="post" onsubmit="return validateForm();">
        <!-- 店舗名入力 -->
        <label for="storeName">店舗名:</label>
        <input type="text" name="store_name" id="store_name" required>

        <!-- 機種名入力 -->
        <label for="model_name1">機種名1:</label>
        <input type="text" id="model_name1" name="model_name1">
        <label for="model_name2">機種名2:</label>
        <input type="text" id="model_name2" name="model_name2">
        <label for="model_name3">機種名3:</label>
        <input type="text" id="model_name3" name="model_name3">

        <!-- 登録ボタン -->
        <button type="submit">登録</button>
    </form>

    <!-- ホーム画面に戻るボタン -->
    <br>
    <div style="text-align: center;">
        <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>
    </div>
</body>
</html>
