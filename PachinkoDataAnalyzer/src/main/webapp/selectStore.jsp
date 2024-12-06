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
    <title>店舗選択</title>
</head>
<body>
    <h1>店舗選択</h1>
    <form action="selectModel.jsp" method="get">
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
        <button type="submit">次へ</button>
    </form>
</body>
</html>
