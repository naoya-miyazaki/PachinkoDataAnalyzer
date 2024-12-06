package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreDAO {

    // 店舗名から店舗IDを取得
    public int getStoreIdByName(Connection con, String storeName) throws SQLException {
        String sql = "SELECT id FROM stores WHERE store_name = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, storeName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; // 店舗が存在しない場合は-1を返す
    }

    // 店舗名を新規登録
    public void insertStore(Connection con, String storeName) throws SQLException {
        String sql = "INSERT INTO stores (store_name) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, storeName);
            stmt.executeUpdate();
        }
    }
}
