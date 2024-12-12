<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>データ閲覧</title>
</head>
<body>
    <h1>${storeName} のデータを閲覧するために、機種と日付を選択してください</h1>

    <!-- 日付と機種を選択するフォーム -->
    <form action="viewDataResult" method="get">
        <label for="model">機種を選択:</label>
        <select name="model_name">
    <option value="all">すべての機種</option> <!-- すべての機種を選択 -->
    <c:forEach var="model" items="${modelNames}">
        <option value="${model}">${model}</option>
    </c:forEach>
</select>
        
        <label for="date">日付を選択:</label>
        <input type="date" id="date" name="data_date" required>

        <input type="hidden" name="store_name" value="${storeName}">

        <button type="submit">データ表示</button>
    </form>

    <br><br>
    <!-- 他の機種を選択するボタン -->
    <a href="viewData?store_name=${storeName}">
        <button type="button">他の機種を選択する</button>
    </a>
    
    <!-- 他の機種を選択するボタン -->
    <a href="storeAction?store_name=${storeName}">
        <button type="button">店舗操作へ戻る</button>
    </a>
</body>
</html>
