<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %> 
<%@ page import="javax.naming.*" %> 
<%@ page import="javax.sql.DataSource" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>データ結果表示</title>
</head>
<body>
    <h1>データ結果表示</h1>
    <%
        // パラメータを取得
        String storeIdStr = request.getParameter("store_id");
        String modelIdStr = request.getParameter("model_name");
        String dataDateStr = request.getParameter("data_date");

        // 必要な変数を初期化
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 店舗IDと機種IDを整数型に変換
            int storeId = Integer.parseInt(storeIdStr);
            int modelId = Integer.parseInt(modelIdStr);

            // データベース接続
            Context initialContext = new InitialContext();
            DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/pachinkoDB");
            con = ds.getConnection();

            // データ取得クエリ
            String sql = "SELECT machine_number, total_games, diff_medals, bb, rb, combined_prob, bb_prob, rb_prob " +
                         "FROM store_data " +
                         "WHERE store_id = ? AND model_id = ? AND data_date = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, modelId);
            pstmt.setDate(3, Date.valueOf(dataDateStr)); // 日付をSQL Date型に変換
            rs = pstmt.executeQuery();

            // データがあるか確認しながら表示
            if (!rs.isBeforeFirst()) {
                // 結果がない場合
    %>
                <p>該当するデータが見つかりませんでした。</p>
    <%
            } else {
    %>
                <table border="1">
                    <thead>
                        <tr>
                            <th>台番号</th>
                            <th>総ゲーム数</th>
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
                while (rs.next()) {
    %>
                    <tr>
                        <td><%= rs.getInt("machine_number") %></td>
                        <td><%= rs.getInt("total_games") %></td>
                        <td><%= rs.getInt("diff_medals") %></td>
                        <td><%= rs.getInt("bb") %></td>
                        <td><%= rs.getInt("rb") %></td>
                        <td><%= rs.getString("combined_prob") %></td>
                        <td><%= rs.getString("bb_prob") %></td>
                        <td><%= rs.getString("rb_prob") %></td>
                    </tr>
    <%
                }
    %>
                    </tbody>
                </table>
    <%
            }
        } catch (SQLException e) {
            out.println("<p>SQLエラー: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } catch (Exception e) {
            out.println("<p>エラー: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } finally {
            // リソースをクローズ
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException ignore) {}
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
    %>
    <br>
    <a href="index.jsp">トップページに戻る</a>
</body>
</html>
