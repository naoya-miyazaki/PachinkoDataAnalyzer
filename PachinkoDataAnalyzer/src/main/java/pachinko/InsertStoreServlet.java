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
        String message;

        // 店舗名のバリデーション
        if (storeName == null || storeName.trim().isEmpty()) {
            message = "エラー: 店舗名を入力してください。";
            forwardWithMessage(request, response, message);
            return;
        }

        // 機種名のバリデーション
        if (modelNames == null || modelNames.trim().isEmpty()) {
            message = "エラー: 機種名を入力してください。";
            forwardWithMessage(request, response, message);
            return;
        }

        try (Connection con = getDatabaseConnection()) {
            // 店舗登録 (店舗名を stores テーブルに登録)
            String insertStoreSQL = "INSERT INTO stores (store_name) VALUES (?)";
            try (PreparedStatement pstmtStore = con.prepareStatement(insertStoreSQL)) {
                pstmtStore.setString(1, storeName.trim());
                pstmtStore.executeUpdate();
            }

            // 店舗IDの取得
            String getStoreIdSQL = "SELECT id FROM stores WHERE store_name = ?";
            int storeId = -1;
            try (PreparedStatement pstmtGetStoreId = con.prepareStatement(getStoreIdSQL)) {
                pstmtGetStoreId.setString(1, storeName.trim());
                try (ResultSet rs = pstmtGetStoreId.executeQuery()) {
                    if (rs.next()) {
                        storeId = rs.getInt("id");
                    }
                }
            }

            if (storeId == -1) {
                message = "エラー: 店舗IDの取得に失敗しました。";
                forwardWithMessage(request, response, message);
                return;
            }

            // 機種名の処理 (複数機種名をカンマ区切りで処理)
            String[] modelNamesArray = modelNames.split(",");
            for (String modelName : modelNamesArray) {
                modelName = modelName.trim();

                // 機種が model_list テーブルに登録されていない場合は登録
                String insertModelSQL = "INSERT INTO model_list (model_name) " +
                                        "SELECT ? WHERE NOT EXISTS (SELECT 1 FROM model_list WHERE model_name = ?)";
                try (PreparedStatement pstmtInsertModel = con.prepareStatement(insertModelSQL)) {
                    pstmtInsertModel.setString(1, modelName);
                    pstmtInsertModel.setString(2, modelName);
                    pstmtInsertModel.executeUpdate();
                }

                // 正しい model_id を取得する
                String getModelIdSQL = "SELECT id FROM model_list WHERE model_name = ?";
                int modelId = -1;
                try (PreparedStatement pstmtGetModelId = con.prepareStatement(getModelIdSQL)) {
                    pstmtGetModelId.setString(1, modelName);
                    try (ResultSet rs = pstmtGetModelId.executeQuery()) {
                        if (rs.next()) {
                            modelId = rs.getInt("id");
                        }
                    }
                }

                if (modelId == -1) {
                    message = "エラー: 機種IDの取得に失敗しました。";
                    forwardWithMessage(request, response, message);
                    return;
                }

                // store_model テーブルに店舗IDと機種IDを関連付けて登録
                String insertStoreModelSQL = "INSERT INTO store_model (store_id, model_id) " +
                                             "VALUES (?, ?)";
                try (PreparedStatement pstmtInsertStoreModel = con.prepareStatement(insertStoreModelSQL)) {
                    pstmtInsertStoreModel.setInt(1, storeId);
                    pstmtInsertStoreModel.setInt(2, modelId);
                    pstmtInsertStoreModel.executeUpdate();
                }
            }

            message = "店舗「" + storeName + "」と機種名を登録しました。";
        } catch (SQLException e) {
            message = "エラー: データベースエラーが発生しました。 (" + e.getMessage() + ")";
        } catch (NamingException e) {
            message = "エラー: データベース接続エラーが発生しました。 (" + e.getMessage() + ")";
        }

        // メッセージをリクエストスコープに設定してフォワード
        forwardWithMessage(request, response, message);
    }

    private Connection getDatabaseConnection() throws NamingException, SQLException {
        Context initialContext = new InitialContext();
        DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/pachinkoDB");
        return ds.getConnection();
    }

    private void forwardWithMessage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher("storeResult.jsp").forward(request, response);
    }
}
