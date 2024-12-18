<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>データ閲覧</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            color: #007bff;
        }
        form {
            max-width: 600px;
            margin: 0 auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin-top: 10px;
            font-weight: bold;
        }
        select, input[type="date"], button {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        button {
            background-color: #007bff;
            color: #fff;
            font-size: 16px;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #0056b3;
        }
        .buttons {
            text-align: center;
            margin-top: 20px;
        }
        .buttons a {
            text-decoration: none;
        }
        .buttons button {
            width: auto;
            margin: 0 10px;
            padding: 10px 20px;
        }
    </style>
    <script>
        function confirmSelection(action) {
            const message = action === 'display' 
                ? "選択したデータを表示します。よろしいですか？"
                : "他の機種を選択します。続行しますか？";
            return confirm(message);
        }
    </script>
</head>
<body>
    <h1>${storeName} のデータを閲覧するために、機種と日付を選択してください</h1>

    <!-- 日付と機種を選択するフォーム -->
    <form action="viewDataResult" method="get">
        <label for="model">機種を選択:</label>
        <select name="model_name" id="model">
            <option value="all">すべての機種</option>
            <c:forEach var="model" items="${modelNames}">
                <option value="${model}">${model}</option>
            </c:forEach>
        </select>

        <label for="date">日付を選択:</label>
        <input type="date" id="date" name="data_date" required>

        <input type="hidden" name="store_name" value="${storeName}">

        <button type="submit" onclick="return confirmSelection('display')">データ表示</button>
    </form>

    <div class="buttons">
        <a href="viewData?store_name=${storeName}">
            <button type="button" onclick="return confirmSelection('select')">他の機種を選択する</button>
        </a>
        <a href="storeAction?store_name=${storeName}">
            <button type="button">店舗操作へ戻る</button>
        </a>
    </div>
</body>
</html>
