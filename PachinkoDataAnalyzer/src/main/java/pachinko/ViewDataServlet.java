package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/viewData")
public class ViewDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String dataDate = request.getParameter("data_date");

        List<Map<String, String>> machineDataList = new ArrayList<>();
        try (Connection con = getDatabaseConnection()) {
            String sql = "SELECT m.machine_number, m.total_games, m.diff_medals, m.bb, m.rb, m.combined_prob, m.bb_prob, m.rb_prob "
                       + "FROM machine_data m "
                       + "JOIN store_data s ON m.store_id = s.store_id "
                       + "WHERE s.store_name = ? AND s.data_date = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, storeName);
                pstmt.setDate(2, Date.valueOf(dataDate));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                	Map<String, String> dataMap = new HashMap<>();
                	dataMap.put("machineNumber", rs.getString("machine_number")); // 台番号
                	dataMap.put("totalGames", rs.getString("total_games"));       // G数
                	dataMap.put("diffMedals", rs.getString("diff_medals"));       // 差枚
                	dataMap.put("bb", rs.getString("bb"));                       // BB
                	dataMap.put("rb", rs.getString("rb"));                       // RB
                	dataMap.put("combinedProb", rs.getString("combined_prob"));  // 合成確率
                	dataMap.put("bbProb", rs.getString("bb_prob"));              // BB確率
                	dataMap.put("rbProb", rs.getString("rb_prob"));              // RB確率
                	machineDataList.add(dataMap);
                }
            }
        } catch (Exception e) {
            throw new ServletException("データ取得エラー: " + e.getMessage(), e);
        }

        request.setAttribute("machineDataList", machineDataList);
        request.setAttribute("storeName", storeName);
        request.setAttribute("dataDate", dataDate);
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewDataResult.jsp");
        dispatcher.forward(request, response);
    }

    private Connection getDatabaseConnection() throws NamingException, SQLException {
        Context initialContext = new InitialContext();
        DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/pachinkoDB");
        return ds.getConnection();
    }
}
