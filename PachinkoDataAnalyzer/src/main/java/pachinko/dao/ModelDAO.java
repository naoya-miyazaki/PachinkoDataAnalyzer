package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pachinko.util.DBUtil;

public class ModelDAO {

    // 店舗名から機種名を取得するメソッド
    public List<String> getModelsByStoreName(String storeName) {
        List<String> modelNames = new ArrayList<>();
        String sql = "SELECT m.model_name FROM model_list m " +
                     "JOIN store_model sm ON m.id = sm.model_id " +
                     "JOIN stores s ON sm.store_id = s.id WHERE s.store_name = ?";
        try (Connection conn = DBUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, storeName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modelNames.add(rs.getString("model_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelNames;
    }

    // 機種名から機種IDを取得するメソッド
    public int getModelIdByName(Connection con, String modelName) throws SQLException {
        String query = "SELECT id FROM model_list WHERE model_name = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, modelName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1; // 機種が存在しない場合
            }
        }
    }

    // 機種名をDBに挿入するメソッド
    public void insertModel(Connection con, String modelName) throws SQLException {
        String query = "INSERT INTO model_list (model_name) VALUES (?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, modelName);
            pst.executeUpdate();
        }
    }
}
