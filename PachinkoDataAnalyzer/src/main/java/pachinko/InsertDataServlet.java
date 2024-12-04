package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/insertData")
public class InsertDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 入力パラメータの取得
        String storeName = request.getParameter("store_name");
        String dataDate = request.getParameter("data_date");
        String modelName = request.getParameter("model_name"); // 機種名
        String pachinkoData = request.getParameter("pachinko_data");

        // 文字エンコーディングの設定
        request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8"); 
        response.setContentType("text/html; charset=UTF-8");

        // 必須項目チェック
        if (storeName == null || storeName.isEmpty() || 
            dataDate == null || dataDate.isEmpty() || 
            modelName == null || modelName.isEmpty() || 
            pachinkoData == null || pachinkoData.isEmpty()) {
            response.getWriter().println("店舗名、日付、機種名、台データは必須です。");
            return;
        }

        // 重複チェック
        boolean isDuplicate = checkDateDuplicate(storeName, dataDate, modelName);
        if (isDuplicate) {
            response.sendRedirect("insertDataResult.jsp?result=duplicate");
            return;
        }

        // 店舗名と機種名からIDを取得
        int storeId = getStoreId(storeName);
        int modelId = getModelId(modelName);

        // 台データの格納
        String[] rows = pachinkoData.split("\n");  // 改行で各行を分ける

        for (String row : rows) {
            String[] columns = row.split("\\s+");  // 空白やタブで分割

            // 必要な項目がすべて揃っているかチェック
            if (columns.length >= 8) {
                try {
                    int machineNumber = Integer.parseInt(columns[0].replace(",", "").trim());  // 台番号
                    int totalGames = Integer.parseInt(columns[1].replace(",", "").trim());     // G数
                    int diffMedals = Integer.parseInt(columns[2].replace(",", "").trim());     // 差枚
                    int bb = Integer.parseInt(columns[3].replace(",", "").trim());             // BB
                    int rb = Integer.parseInt(columns[4].replace(",", "").trim());             // RB
                    String combinedProb = columns[5].trim();  // 合成確率
                    String bbProb = columns[6].trim();        // BB確率
                    String rbProb = columns[7].trim();        // RB確率

                    // 台データをstore_dataに格納
                    insertMachineData(storeId, modelId, machineNumber, totalGames, diffMedals, bb, rb, combinedProb, bbProb, rbProb, dataDate);
                } catch (NumberFormatException e) {
                    response.getWriter().println("無効なデータが含まれています。");
                    return;
                }
            }
        }

        // 登録が成功したことを結果画面に伝えるために、リダイレクト
        response.sendRedirect("insertDataResult.jsp?result=success");
    }

    // 日付と店舗、機種の重複チェック
    private boolean checkDateDuplicate(String storeName, String dataDate, String modelName) throws ServletException {
        try (Connection con = getDatabaseConnection()) {
            // store_dataテーブルで重複チェック
            String sql = "SELECT 1 FROM store_data sd "
                       + "JOIN stores s ON s.id = sd.store_id "
                       + "JOIN model_list m ON m.id = sd.model_id "
                       + "WHERE s.store_name = ? AND m.model_name = ? AND sd.data_date = CAST(? AS DATE)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, storeName);
                pstmt.setString(2, modelName);
                pstmt.setString(3, dataDate); // 文字列として渡す
                ResultSet rs = pstmt.executeQuery();
                return rs.next();  // 重複があれば結果が返ってくるのでtrueを返す
            }
        } catch (SQLException | NamingException e) {
            throw new ServletException("重複チェックのエラー: " + e.getMessage(), e);
        }
    }

    // 店舗名からstore_idを取得
    private int getStoreId(String storeName) throws ServletException {
        try (Connection con = getDatabaseConnection()) {
            String sql = "SELECT id FROM stores WHERE store_name = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, storeName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new ServletException("指定された店舗名が存在しません: " + storeName);
                }
            }
        } catch (SQLException | NamingException e) {
            throw new ServletException("店舗ID取得エラー: " + e.getMessage(), e);
        }
    }

    // 機種名からmodel_idを取得
    private int getModelId(String modelName) throws ServletException {
        try (Connection con = getDatabaseConnection()) {
            String sql = "SELECT id FROM model_list WHERE model_name = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, modelName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new ServletException("指定された機種名が存在しません: " + modelName);
                }
            }
        } catch (SQLException | NamingException e) {
            throw new ServletException("機種ID取得エラー: " + e.getMessage(), e);
        }
    }

    // 台データをstore_dataテーブルに挿入
    private void insertMachineData(int storeId, int modelId, int machineNumber, int totalGames, int diffMedals, int bb, int rb, String combinedProb, String bbProb, String rbProb, String dataDate) throws ServletException {
        try (Connection con = getDatabaseConnection()) {
            String sql = "INSERT INTO store_data (store_id, model_id, data_date, machine_number, total_games, diff_medals, bb, rb, combined_prob, bb_prob, rb_prob) "
                       + "VALUES (?, ?, CAST(? AS DATE), ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, storeId);
                pstmt.setInt(2, modelId);
                pstmt.setString(3, dataDate);
                pstmt.setInt(4, machineNumber);
                pstmt.setInt(5, totalGames);
                pstmt.setInt(6, diffMedals);
                pstmt.setInt(7, bb);
                pstmt.setInt(8, rb);
                pstmt.setString(9, combinedProb);
                pstmt.setString(10, bbProb);
                pstmt.setString(11, rbProb);
                pstmt.executeUpdate();
            }
        } catch (SQLException | NamingException e) {
            throw new ServletException("台データの登録エラー: " + e.getMessage(), e);
        }
    }

    // データベース接続メソッド
    private Connection getDatabaseConnection() throws NamingException, SQLException {
        Context initialContext = new InitialContext();
        DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/pachinkoDB");
        return ds.getConnection();
    }
}
