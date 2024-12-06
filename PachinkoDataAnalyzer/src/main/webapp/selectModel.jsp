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
    <title>機種と日付選択</title>
</head>
<body>
    <h1>機種と日付選択</h1>
    <form action="viewDataResult.jsp" method="get">
        <!-- 店舗IDを保持 -->
        <input type="hidden" name="store_id" value="<%= request.getParameter("store_name") %>">
        
        <label for="model_name">機種名:</label>
        <select name="model_name" id="model_name" required>
            <option value="" disabled selected>機種名を選択</option>
            <%
                // 店舗IDを取得し、型変換
                String storeIdStr = request.getParameter("store_name");
                int storeId = 0;
                try {
                    storeId = Integer.parseInt(storeIdStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
            %>
                <option disabled>無効な店舗IDです</option>
            <%
                }

                Connection con = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    // データベース接続
                    Context initialContext = new InitialContext();
                    DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/pachinkoDB");
                    con = ds.getConnection();

                    // 店舗IDに紐づく機種IDを取得するSQL
                    pstmt = con.prepareStatement(
                        "SELECT m.id, m.model_name " +
                        "FROM model_list m " +
                        "JOIN store_model sm ON m.id = sm.model_id " +
                        "WHERE sm.store_id = ?"
                    );
                    pstmt.setInt(1, storeId);  // 店舗IDをセット
                    rs = pstmt.executeQuery();
                    
                    // 機種名のリストを表示
                    boolean hasData = false;
                    while (rs.next()) {
                        hasData = true;
            %>
                        <option value="<%= rs.getInt("id") %>"><%= rs.getString("model_name") %></option>
            <%
                    }
                    if (!hasData) { // 結果がなかった場合
            %>
                        <option disabled>該当する機種はありません</option>
            <%
                    }
                } catch (SQLException e) {
                    // SQLエラーが発生した場合、詳細を表示
                    e.printStackTrace();
            %>
                <option disabled>SQLエラーが発生しました: <%= e.getMessage() %></option>
            <%
                } catch (Exception e) {
                    // その他のエラーが発生した場合
                    e.printStackTrace();
            %>
                <option disabled>エラーが発生しました: <%= e.getMessage() %></option>
            <%
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                    if (pstmt != null) try { pstmt.close(); } catch (SQLException ignore) {}
                    if (con != null) try { con.close(); } catch (SQLException ignore) {}
                }
            %>
        </select>
        <br><br>

        <label for="data_date">日付:</label>
        <input type="date" name="data_date" id="data_date" required>
        <br><br>

        <button type="submit">結果表示</button>
    </form>
</body>
</html>
