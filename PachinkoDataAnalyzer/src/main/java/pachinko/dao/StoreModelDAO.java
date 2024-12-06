package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreModelDAO {
    public boolean isStoreModelDuplicate(Connection con, int storeId, int modelId) throws SQLException {
        String sql = "SELECT 1 FROM store_model WHERE store_id = ? AND model_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setInt(2, modelId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // 既に存在する場合、trueを返す
            }
        }
    }

    public void insertStoreModel(Connection con, int storeId, int modelId) throws SQLException {
        String sql = "INSERT INTO store_model (store_id, model_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setInt(2, modelId);
            ps.executeUpdate();
        }
    }
}
