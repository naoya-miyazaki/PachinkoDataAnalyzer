<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>データ閲覧結果</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            color: #007bff;
        }
        .model-header {
            font-size: 20px;
            font-weight: bold;
            color: #007bff;
            margin-top: 20px;
        }
        .data-row {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            justify-content: space-around;
            margin-top: 20px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: center;
        }
        th {
            background-color: #007bff;
            color: #fff;
        }
        button {
            background-color: #007bff;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            text-align: center;
            margin-top: 20px;
        }
        button:hover {
            background-color: #0056b3;
        }
        .buttons {
            text-align: center;
            margin-top: 30px;
        }
    </style>
    <script>
        function confirmAction(message) {
            return confirm(message);
        }
    </script>
</head>
<body>

    <h1>${storeName} のデータ結果</h1>

    <c:if test="${not empty groupedData}">
        <div class="data-row">
            <c:forEach var="entry" items="${groupedData}">
                <div class="data-cell">
                    <h2 class="model-header">機種名: ${entry.key}</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>台番号</th>
                                <th>G数</th>
                                <th>差枚</th>
                                <th>BB</th>
                                <th>RB</th>
                                <th>合成確率</th>
                                <th>BB確率</th>
                                <th>RB確率</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="data" items="${entry.value}">
                                <tr>
                                    <td>${data.machineNumber}</td>
                                    <td>${data.totalGames}</td>
                                    <td>${data.diffMedals}</td>
                                    <td>${data.bb}</td>
                                    <td>${data.rb}</td>
                                    <td>${data.combinedProb}</td>
                                    <td>${data.bbProb}</td>
                                    <td>${data.rbProb}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty groupedData}">
        <p>該当するデータがありません。</p>
    </c:if>

    <div class="buttons">
        <a href="viewData?store_name=${storeName}"><button type="button" onclick="return confirmAction('戻りますか？')">戻る</button></a>
        <a href="viewData?store_name=${storeName}&model_name=all&data_date=${date}"><button type="button" onclick="return confirmAction('他の機種を選択しますか？')">他の機種を選択する</button></a>
    </div>

</body>
</html>
