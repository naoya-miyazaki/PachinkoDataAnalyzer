<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>データ登録結果</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            color: #343a40;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            margin-top: 20px;
            color: #007bff;
        }
        p {
            text-align: center;
            font-size: 1.2em;
            margin-top: 20px;
        }
        .buttons {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 30px;
        }
        button, a {
            display: inline-block;
            text-decoration: none;
            color: white;
            background-color: #007bff;
            padding: 10px 20px;
            font-size: 1em;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover, a:hover {
            background-color: #0056b3;
        }
        a {
            line-height: 36px;
        }
    </style>
    <script>
        function confirmContinue() {
            return confirm("続けてデータを入力しますか？");
        }
    </script>
</head>
<body>
    <h1>データ登録結果</h1>
    
    <!-- メッセージを表示 -->
    <p>${message}</p>

    <div class="buttons">
        <a href="insertData?store_name=${storeName}" onclick="return confirmContinue();">
            続けてデータを入力する
        </a>
        <a href="index.jsp">メイン画面に戻る</a>
    </div>
</body>
</html>
