package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/insertStore")
public class InsertStoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String modelNames = request.getParameter("model_names");
        
        // 店舗名が空でないことを確認
        if (storeName == null || storeName.isEmpty()) {
            response.getWriter().write("店舗名が入力されていません。");
            return;
        }
        
        // 入力された機種名が空でないことを確認
        if (modelNames == null || modelNames.isEmpty()) {
            response.getWriter().write("機種名が入力されていません。");
            return;
        }

        // 店舗名が既に存在するか確認するクエリ
        String checkStoreQuery = "SELECT COUNT(*) FROM stores WHERE store_name = ?";
        
        try (Connection con = getConnection(); 
             PreparedStatement pst = con.prepareStatement(checkStoreQuery)) {
            pst.setString(1, storeName);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // 既に店舗が存在する場合
                response.getWriter().write("エラー: 店舗名 '" + storeName + "' は既に登録されています。");
                return;
            }

            // 新しい店舗を追加するクエリ
            String insertStoreQuery = "INSERT INTO stores (store_name) VALUES (?)";
            try (PreparedStatement pstInsert = con.prepareStatement(insertStoreQuery)) {
                pstInsert.setString(1, storeName);
                pstInsert.executeUpdate();
            }

            // 機種名を登録する
            String[] modelNameList = modelNames.split(",");
            String insertModelQuery = "INSERT INTO model_list (model_name) VALUES (?) ON CONFLICT (model_name) DO NOTHING";
            try (PreparedStatement pstInsertModel = con.prepareStatement(insertModelQuery)) {
                for (String modelName : modelNameList) {
                    pstInsertModel.setString(1, modelName.trim());
                    pstInsertModel.executeUpdate();
                }
            }

            // 店舗と機種を関連付ける
            String getStoreIdQuery = "SELECT id FROM stores WHERE store_name = ?";
            try (PreparedStatement pstGetStoreId = con.prepareStatement(getStoreIdQuery)) {
                pstGetStoreId.setString(1, storeName);
                ResultSet storeRs = pstGetStoreId.executeQuery();
                if (storeRs.next()) {
                    int storeId = storeRs.getInt("id");

                    for (String modelName : modelNameList) {
                        String getModelIdQuery = "SELECT id FROM model_list WHERE model_name = ?";
                        try (PreparedStatement pstGetModelId = con.prepareStatement(getModelIdQuery)) {
                            pstGetModelId.setString(1, modelName.trim());
                            ResultSet modelRs = pstGetModelId.executeQuery();
                            if (modelRs.next()) {
                                int modelId = modelRs.getInt("id");

                                String insertStoreModelQuery = "INSERT INTO store_model (store_id, model_id) VALUES (?, ?)";
                                try (PreparedStatement pstInsertStoreModel = con.prepareStatement(insertStoreModelQuery)) {
                                    pstInsertStoreModel.setInt(1, storeId);
                                    pstInsertStoreModel.setInt(2, modelId);
                                    pstInsertStoreModel.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }

            // 成功メッセージを表示
            response.getWriter().write("店舗 '" + storeName + "' と機種名を登録しました。");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("データベースエラーが発生しました: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/PostgreSQL");
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException("データベース接続エラー", e);
        }
    }
}
