package pachinko.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pachinko.util.DBUtil;

public class StoreDataDAO {
	
	 public boolean insertStoreData(Connection con, int storeId, int modelId, Date dataDate,
             int machineNumber, int totalGames, int diffMedals, 
             int bb, int rb, double combinedProb, double bbProb, double rbProb) {
		 String insertSQL = "INSERT INTO store_data (store_id, model_id, data_date, machine_number, " +
				 "total_games, diff_medals, bb, rb, combined_prob, bb_prob, rb_prob) " +
				 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement pst = con.prepareStatement(insertSQL)) {
			pst.setInt(1, storeId);
			pst.setInt(2, modelId);
			pst.setDate(3, dataDate);
			pst.setInt(4, machineNumber);
			pst.setInt(5, totalGames);
			pst.setInt(6, diffMedals);
			pst.setInt(7, bb);
			pst.setInt(8, rb);
			pst.setDouble(9, combinedProb);
			pst.setDouble(10, bbProb);
			pst.setDouble(11, rbProb);

			int affectedRows = pst.executeUpdate();
			return affectedRows > 0;  // 成功したらtrueを返す
	} catch (SQLException e) {
			e.printStackTrace();
			return false;  // エラーが発生した場合はfalseを返す
	}
}

	// StoreDataDAO.java
	public boolean checkDuplicateData(int storeId, int modelId, int machineNumber, String dataDate) {
	    String query = "SELECT COUNT(*) FROM store_data WHERE store_id = ? AND model_id = ? AND machine_number = ? AND data_date = ?";
	    
	    try (Connection con = DBUtil.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        
	        ps.setInt(1, storeId);
	        ps.setInt(2, modelId);
	        ps.setInt(3, machineNumber);
	        ps.setString(4, dataDate);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            return true; // 重複データが存在
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // 重複データは存在しない
	}


    // データをstore_dataテーブルに挿入するメソッド
 // StoreDataDAO.java
    public void insertData(int storeId, int modelId, int machineNumber, String dataDate,
                           String totalGames, String diffMedals, String bbCount, String rbCount, 
                           String combinedProb, String bbProb, String rbProb) {
        String query = "INSERT INTO store_data (store_id, model_id, machine_number, data_date, " +
                       "total_games, diff_medals, bb, rb, combined_prob, bb_prob, rb_prob) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, storeId);
            ps.setInt(2, modelId);
            ps.setInt(3, machineNumber);
            ps.setString(4, dataDate);
            ps.setString(5, totalGames);
            ps.setString(6, diffMedals);
            ps.setString(7, bbCount);
            ps.setString(8, rbCount);
            ps.setString(9, combinedProb);
            ps.setString(10, bbProb);
            ps.setString(11, rbProb);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isDataDuplicate(Connection con, int storeId, int modelId, java.sql.Date dataDate, int machineNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM store_data WHERE store_id = ? AND model_id = ? AND data_date = ? AND machine_number = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            stmt.setInt(2, modelId);
            stmt.setDate(3, dataDate);
            stmt.setInt(4, machineNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // 重複している場合はtrue
                }
            }
        }
        return false; // 重複していない場合はfalse
    }

}
