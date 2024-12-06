package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class StoreDataDAO {

    // 重複チェック（店舗名、日付、機種名）
    public boolean checkDuplicate(String storeName, String dataDate, String modelName) throws SQLException, NamingException {
        try (Connection con = getConnection()) {
            String sql = "SELECT 1 FROM store_data sd "
                       + "JOIN stores s ON s.id = sd.store_id "
                       + "JOIN model_list m ON m.id = sd.model_id "
                       + "WHERE s.store_name = ? AND m.model_name = ? AND sd.data_date = CAST(? AS DATE)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, storeName);
                pstmt.setString(2, modelName);
                pstmt.setString(3, dataDate);
                ResultSet rs = pstmt.executeQuery();
                return rs.next();  // 重複があれば結果が返ってくるのでtrueを返す
            }
        }
    }

    // 台データをstore_dataテーブルに挿入
    public void insertMachineData(int storeId, int modelId, int machineNumber, int totalGames, int diffMedals, int bb, int rb, String combinedProb, String bbProb, String rbProb, String dataDate) throws SQLException, NamingException {
        try (Connection con = getConnection()) {
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
        }
    }

    private Connection getConnection() throws SQLException, NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/pachinkoDB");
        return ds.getConnection();
    }
}
