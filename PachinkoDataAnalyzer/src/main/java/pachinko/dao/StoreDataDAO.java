package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import StoreData;

public class StoreDataDAO {
    private Connection getConnection() throws SQLException {
        // データベース接続設定（既存の内容を流用してください）
    }

    public List<StoreData> getDataByStoreAndDate(String storeName, String dataDate) {
        List<StoreData> dataList = new ArrayList<>();
        String sql = """
            SELECT * FROM store_data 
            WHERE store_id = (SELECT id FROM stores WHERE store_name = ?) 
              AND data_date = ?""";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, storeName);
            ps.setString(2, dataDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataList.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public List<StoreData> getDataByStoreDateAndModel(String storeName, String dataDate, String modelName) {
        List<StoreData> dataList = new ArrayList<>();
        String sql = """
            SELECT * FROM store_data 
            WHERE store_id = (SELECT id FROM stores WHERE store_name = ?) 
              AND model_id = (SELECT id FROM model_list WHERE model_name = ?)
              AND data_date = ?""";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, storeName);
            ps.setString(2, modelName);
            ps.setString(3, dataDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataList.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private StoreData mapRow(ResultSet rs) throws SQLException {
        StoreData data = new StoreData();
        data.setMachineNumber(rs.getInt("machine_number"));
        data.setTotalGames(rs.getInt("total_games"));
        data.setDiffMedals(rs.getInt("diff_medals"));
        data.setBb(rs.getInt("bb"));
        data.setRb(rs.getInt("rb"));
        data.setCombinedProb(rs.getString("combined_prob"));
        data.setBbProb(rs.getString("bb_prob"));
        data.setRbProb(rs.getString("rb_prob"));
        return data;
    }
}
