<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>データがありません</title>
</head>
<body>
    <h1>データがありません</h1>
    <p>指定した条件のデータは見つかりませんでした。</p>
    <a href="viewData.jsp?store_name=${storeName}">
        <button type="button">他の機種を選択する</button>
    </a>
</body>
</html>
