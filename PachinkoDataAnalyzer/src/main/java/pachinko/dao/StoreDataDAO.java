package pachinko.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pachinko.model.StoreData; // StoreDataクラスがない場合は別途定義が必要
import pachinko.util.DBUtil; // DB接続ユーティリティ

public class StoreDataDAO {

    // DB接続はDBUtilを使用
    private Connection getConnection() throws SQLException {
        return DBUtil.getConnection(); // DBUtilから接続を取得
    }

 // 店舗名と日付に基づいてデータを取得
    public List<StoreData> getDataByStoreAndDate(String storeName, String dataDate) {
        List<StoreData> dataList = new ArrayList<>();
        String sql = """
            SELECT * FROM store_data 
            WHERE store_id = (SELECT id FROM stores WHERE store_name = ?) 
              AND data_date = ?""";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, storeName);  // 店舗名をセット
            ps.setString(2, dataDate);   // 日付をセット
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataList.add(mapRow(rs)); // 結果をStoreDataリストに追加
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }


 // ResultSetからStoreDataを作成
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
        // model_nameを追加
        data.setModelName(rs.getString("model_name"));
        return data;
    }

    // 店舗名、日付、機種名に基づいてデータを取得
    public List<StoreData> getDataByStoreDateAndModel(String storeName, String dataDate, String modelName) {
        List<StoreData> dataList = new ArrayList<>();
        String sql = """
            SELECT sd.*, m.model_name FROM store_data sd
            JOIN stores s ON sd.store_id = s.id
            JOIN model_list m ON sd.model_id = m.id
            WHERE s.store_name = ? AND sd.data_date = ?""";

        // 機種名が指定されていればSQLクエリに追加
        if (modelName != null && !"all".equals(modelName)) {
            sql += " AND m.model_name = ?";
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, storeName);  // 店舗名をセット
            ps.setString(2, dataDate);   // 日付をセット

            if (modelName != null && !"all".equals(modelName)) {
                ps.setString(3, modelName); // 機種名をセット
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataList.add(mapRow(rs)); // 結果をStoreDataリストに追加
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
 // データが重複していないか確認するメソッド
    public boolean isDataDuplicate(Connection con, int storeId, int modelId, Date dataDate, int machineNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM store_data WHERE store_id = ? AND model_id = ? AND data_date = ? AND machine_number = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, modelId);
            pstmt.setDate(3, dataDate);
            pstmt.setInt(4, machineNumber);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // データを登録するメソッド
    public boolean insertStoreData(Connection con, int storeId, int modelId, Date dataDate, int machineNumber,
                                    int totalGames, int diffMedals, int bb, int rb, double combinedProb,
                                    double bbProb, double rbProb) throws SQLException {
        String sql = "INSERT INTO store_data (store_id, model_id, data_date, machine_number, total_games, diff_medals, bb, rb, combined_prob, bb_prob, rb_prob) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, modelId);
            pstmt.setDate(3, dataDate);
            pstmt.setInt(4, machineNumber);
            pstmt.setInt(5, totalGames);
            pstmt.setInt(6, diffMedals);
            pstmt.setInt(7, bb);
            pstmt.setInt(8, rb);
            pstmt.setDouble(9, combinedProb);
            pstmt.setDouble(10, bbProb);
            pstmt.setDouble(11, rbProb);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public List<StoreData> getDataByStoreAndModelAndDate(String storeName, String modelName, String dateStr) {
        List<StoreData> storeDataList = new ArrayList<>();
        java.sql.Date date = null;

        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdf.parse(dateStr);
                date = new java.sql.Date(parsedDate.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return storeDataList; // 日付が無効なら空のリストを返す
        }

        String sql = "SELECT sd.*, m.model_name FROM store_data sd " +
                "JOIN stores s ON sd.store_id = s.id " +
                "JOIN model_list m ON sd.model_id = m.id " +
                "WHERE s.store_name = ? AND sd.data_date = ? ";

        if (modelName != null && !"all".equals(modelName)) {
            sql += "AND m.model_name = ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, storeName);
            pstmt.setDate(2, date);

            if (modelName != null && !"all".equals(modelName)) {
                pstmt.setString(3, modelName);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StoreData data = new StoreData();
                    data.setMachineNumber(rs.getInt("machine_number"));
                    data.setTotalGames(rs.getInt("total_games"));
                    data.setDiffMedals(rs.getInt("diff_medals"));
                    data.setBb(rs.getInt("bb"));
                    data.setRb(rs.getInt("rb"));
                    data.setCombinedProb(rs.getString("combined_prob"));
                    data.setBbProb(rs.getString("bb_prob"));
                    data.setRbProb(rs.getString("rb_prob"));
                    data.setModelName(rs.getString("model_name")); // ここで model_name を設定
                    storeDataList.add(data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storeDataList;
    }



}
