<!-- selectAction.jsp -->
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>操作選択</title>
</head>
<body>
    <h1>${storeName} のデータ操作を選択してください</h1>

    <form action="insertData" method="get">
    <input type="submit" value="データ入力">
    <!-- store_nameをクエリパラメータとして渡す -->
    <input type="hidden" name="store_name" value="${storeName}">
</form>
    
   <form action="viewData" method="get">
    <input type="hidden" name="store_name" value="${storeName}">
    <input type="submit" value="データ閲覧">
</form>
    
    <!-- ホーム画面に戻るボタン -->
    <br><br>
    <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>
</body>
</html>
