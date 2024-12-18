<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>トップページ</title>
    <style>
        /* CSS: 全体のスタイル設定 */
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        h1 {
            text-align: center;
            color: #333;
            padding: 20px;
        }
        ul {
            list-style: none;
            padding: 0;
            display: flex;
            justify-content: center;
            gap: 20px;
        }
        li {
            margin: 0;
        }
        a {
            text-decoration: none;
            color: #fff;
            background-color: #007bff;
            padding: 10px 20px;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        a:hover {
            background-color: #0056b3;
        }
    </style>
    
</head>
<body>
    <h1>パチンコデータ管理</h1>
    <ul>
        <li><a href="insertStore.jsp">新規店舗登録</a></li>
        <li><a href="selectExistingStore">既存店舗操作</a></li>
    </ul>
</body>
</html>
