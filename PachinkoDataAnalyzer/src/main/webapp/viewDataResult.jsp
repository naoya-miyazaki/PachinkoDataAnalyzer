<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>データ結果</title>
</head>
<body>
    <h1>データ結果</h1>
    
    <h2>店舗名: ${storeName}</h2>
    <h2>日付: ${dataDate}</h2>
    <h2>機種名: ${modelName}</h2>
    
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
            <c:forEach var="data" items="${storeData}">
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
</body>
</html>
