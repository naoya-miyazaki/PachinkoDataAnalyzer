<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>登録結果</title>
</head>
<body>
    <h1>登録結果</h1>

    <!-- エラーメッセージ表示 -->
    <c:if test="${not empty errorMessage}">
        <div style="color: red;">${errorMessage}</div>
    </c:if>

    <!-- 成功メッセージ表示 -->
    <c:if test="${not empty successMessage}">
        <div style="color: green;">${successMessage}</div>
    </c:if>

    <br>
    <a href="insertStore.jsp">店舗・機種登録画面に戻る</a>
</body>
</html>
