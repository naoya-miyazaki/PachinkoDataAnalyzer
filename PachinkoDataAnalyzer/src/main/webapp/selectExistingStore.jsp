<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>登録店舗の選択</title>
</head>
<body>
    <h1>登録されている店舗を選択してください</h1>

    <!-- メッセージ表示 -->
    <c:if test="${not empty message}">
        <div style="color: red;">
            <p>${message}</p>
        </div>
    </c:if>

    <!-- 店舗選択フォーム -->
    <form action="storeAction" method="get">
    <label for="store_name">店舗名:</label>
    <select name="store_name" id="store_name" required>
        <c:forEach var="store" items="${storeNames}">
            <option value="${store}">${store}</option>
        </c:forEach>
    </select><br>

        <input type="submit" value="選択">
    </form>

    <!-- ホーム画面に戻るボタン -->
    <br><br>
    <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>
</body>
</html>
