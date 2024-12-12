<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>データ閲覧</title>
</head>
<body>
    <h1>データ閲覧</h1>
    <form action="viewDataResult" method="get">
        <input type="hidden" name="store_name" value="${storeName}">
        
        <label for="data_date">日付を選択:</label>
        <input type="date" name="data_date" id="data_date" required><br>
        
        <label for="model_name">機種を選択:</label>
        <select name="model_name" id="model_name">
            <option value="all">すべての機種</option>
            <c:forEach var="model" items="${modelNames}">
                <option value="${model}">${model}</option>
            </c:forEach>
        </select><br><br>
        
        <input type="submit" value="データを表示">
    </form>
</body>
</html>
