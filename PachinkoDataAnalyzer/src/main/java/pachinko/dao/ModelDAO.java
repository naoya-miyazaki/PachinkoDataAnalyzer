package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelDAO {

    // 機種IDを機種名から取得
    public int getModelIdByName(Connection con, String modelName) throws SQLException {
        String sql = "SELECT id FROM model_list WHERE model_name = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, modelName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; // 機種が存在しない場合は-1を返す
    }

    // 機種名がデータベースに存在しない場合、新規登録
    public void insertModel(Connection con, String modelName) throws SQLException {
        String sql = "INSERT INTO model_list (model_name) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, modelName);
            stmt.executeUpdate();
        }
    }
}
