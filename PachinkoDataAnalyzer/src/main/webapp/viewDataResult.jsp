<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %> 
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>データ閲覧結果</title>
</head>
<body>
    <h1>データ閲覧結果</h1>
    <p>店舗名: <%= request.getAttribute("storeName") %></p>
    <p>日付: <%= request.getAttribute("dataDate") %></p>
    <p>機種名: <%= request.getAttribute("modelName") %></p>

    <%
        // 機種データを取得
        List<Map<String, String>> machineDataList = (List<Map<String, String>>) request.getAttribute("machineDataList");
        if (machineDataList == null || machineDataList.isEmpty()) {
    %>
        <p>該当するデータは見つかりませんでした。</p>
    <%
        } else {
    %>
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
                <%
                    for (Map<String, String> data : machineDataList) {
                %>
                    <tr>
                        <td><%= data.get("machine_id") %></td>
                        <td><%= data.get("g_count") %></td>
                        <td><%= data.get("difference") %></td>
                        <td><%= data.get("bb") %></td>
                        <td><%= data.get("rb") %></td>
                        <td><%= data.get("composite_rate") %></td>
                        <td><%= data.get("bb_rate") %></td>
                        <td><%= data.get("rb_rate") %></td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        }
    %>
</body>
</html>
