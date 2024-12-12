<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>データ閲覧結果</title>
    <style>
        .data-row {
            display: flex;
            gap: 10px;
        }
        .data-cell {
            border: 1px solid #ddd;
            padding: 8px;
        }
        .model-header {
            margin-top: 20px;
            font-size: 18px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h1>${storeName} のデータ結果</h1>

    <c:if test="${not empty groupedData}">
    <c:forEach var="entry" items="${groupedData}">
        <h2>機種名: ${entry.key}</h2>
        <table border="1">
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
    </c:forEach>
</c:if>

<c:if test="${empty groupedData}">
    <p>該当するデータがありません。</p>
</c:if>

    <br>
    <a href="viewData?store_name=${storeName}"><button type="button">戻る</button></a>

    <!-- 他の機種を選択するボタン -->
    <a href="viewData?store_name=${storeName}&model_name=all&data_date=${date}">
    <button type="button">他の機種を選択する</button>
</a>

</body>
</html>
