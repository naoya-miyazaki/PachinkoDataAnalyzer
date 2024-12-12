package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pachinko.util.DBUtil;

public class ModelDAO {

	public List<String> getModelsByStoreId(int storeId) throws SQLException {
        List<String> modelNames = new ArrayList<>();
        String query = "SELECT model_name FROM model_list "
                     + "INNER JOIN store_model ON model_list.id = store_model.model_id "
                     + "WHERE store_model.store_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, storeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modelNames.add(rs.getString("model_name"));
            }
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

    public void insertModel(Connection con, String modelName) throws SQLException {
        String query = "INSERT INTO model_list (model_name) VALUES (?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, modelName);
            pst.executeUpdate();
        }
    }
}
