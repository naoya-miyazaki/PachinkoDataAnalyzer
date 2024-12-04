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
    <title>データ閲覧</title>
</head>
<body>
    <h1>データ閲覧</h1>
    <form action="viewData" method="post">
        <label for="store_name">店舗名:</label>
        <select name="store_name" id="store_name" required>
            <option value="" disabled selected>店舗名を選択</option>
            <%
                Connection con = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    Context initialContext = new InitialContext();
                    DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/pachinkoDB");
                    con = ds.getConnection();
                    pstmt = con.prepareStatement("SELECT id, store_name FROM stores");
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
            %>
                        <option value="<%= rs.getInt("id") %>"><%= rs.getString("store_name") %></option>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
            %>
                <option disabled>エラーが発生しました</option>
            <%
                } finally {
                    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                    if (pstmt != null) try { pstmt.close(); } catch (SQLException ignore) {}
                    if (con != null) try { con.close(); } catch (SQLException ignore) {}
                }
            %>
        </select>
        <br><br>

        <label for="model_name">機種名:</label>
        <select name="model_name" id="model_name" required>
            <option value="" disabled selected>機種名を選択</option>
            <%
                try {
                    pstmt = con.prepareStatement("SELECT id, model_name FROM model_list");
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
            %>
                        <option value="<%= rs.getInt("id") %>"><%= rs.getString("model_name") %></option>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
            %>
                <option disabled>エラーが発生しました</option>
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

        <button type="submit">閲覧</button>
    </form>
</body>
</html>
