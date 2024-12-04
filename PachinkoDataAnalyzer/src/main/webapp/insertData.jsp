<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.DataSource" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>データ格納</title>
</head>
<body>
    <h1>台データの登録</h1>
    <form action="insertData" method="post">
        <!-- 店舗名の選択 -->
        <label for="storeName">店舗名:</label>
        <select name="store_name" id="store_name" required>
            <option value="" disabled selected>店舗を選択</option>
            <% 
                try {
                    Context initCtx = new InitialContext();
                    DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/pachinkoDB");
                    Connection con = ds.getConnection();
                    PreparedStatement pstmt = con.prepareStatement("SELECT DISTINCT store_name FROM stores");
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
            %>
            <option value="<%= rs.getString("store_name") %>"><%= rs.getString("store_name") %></option>
            <%
                    }
                    rs.close();
                    pstmt.close();
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
        </select>
        <br><br>

        <!-- 日付の入力 -->
        <label for="dataDate">日付:</label>
        <input type="date" name="data_date" id="data_date" required>
        <br><br>

        <!-- 機種名の選択 -->
        <label for="modelName">機種名:</label>
        <select name="model_name" id="model_name" required>
            <option value="" disabled selected>機種を選択</option>
            <% 
                try {
                    Context initCtx = new InitialContext();
                    DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/pachinkoDB");
                    Connection con = ds.getConnection();
                    PreparedStatement pstmt = con.prepareStatement("SELECT model_name FROM model_list");
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
            %>
            <option value="<%= rs.getString("model_name") %>"><%= rs.getString("model_name") %></option>
            <%
                    }
                    rs.close();
                    pstmt.close();
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
        </select>
        <br><br>

        <!-- 台データの入力 -->
        <label for="machineData">台データ:</label>
        <textarea name="pachinko_data" id="pachinko_data" rows="10" cols="50" 
                  placeholder="台番号\tG数\t差枚\tBB\tRB\t合成確率\tBB確率\tRB確率" required>
        </textarea>
        <br><br>

        <!-- 登録ボタン -->
        <button type="submit">登録</button>
    </form>

    <!-- シンプルにホーム画面に戻るリンク -->
    <br><br>
    <a href="index.jsp"><button type="button">ホーム画面に戻る</button></a>

</body>
</html>
