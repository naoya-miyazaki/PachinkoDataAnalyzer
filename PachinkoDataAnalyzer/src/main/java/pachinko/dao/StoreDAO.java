package pachinko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    // 店舗名リストを取得するメソッド
	public List<String> getStoreNames(Connection con) throws SQLException {
	    List<String> storeNames = new ArrayList<>();
	    String query = "SELECT store_name FROM stores";  // storesテーブルから店舗名を取得

	    try (PreparedStatement pst = con.prepareStatement(query);
	         ResultSet rs = pst.executeQuery()) {

	        while (rs.next()) {
	            storeNames.add(rs.getString("store_name"));
	        }
	    }

	    return storeNames;
	}

 // 店舗名から店舗IDを取得するメソッド
	public int getStoreIdByName(Connection conn, String storeName) throws SQLException {
        String query = "SELECT id FROM stores WHERE TRIM(store_name) = TRIM(?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, storeName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; // 店舗が見つからない場合
    
    }

    public void insertStore(Connection con, String storeName) throws SQLException {
        String query = "INSERT INTO stores (store_name) VALUES (?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
        	pst.setString(1, storeName.trim()); // 空白削除
            pst.executeUpdate();
        }
    }
}