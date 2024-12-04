<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>パチンコデータ管理</title>
</head>
<body>
    <h1>パチンコデータ管理システム</h1>

    <p>以下の操作を選択してください:</p>

    <!-- 店舗登録 -->
    <h2>新規店舗登録</h2>
    <a href="insertStore.jsp"><button type="button">店舗登録</button></a>
    
    <!-- データを入力して格納 -->
    <h2>データ操作</h2>
    <form action="insertData.jsp" method="get" style="margin-bottom: 20px;">
        <button type="submit">データを入力して格納</button>
    </form>

    <!-- 格納済みデータを閲覧 -->
    <form action="viewData.jsp" method="get">
        <button type="submit">格納済みデータを閲覧</button>
    </form>
</body>
</html>
