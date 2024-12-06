package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import pachinko.dao.ModelDAO;
import pachinko.dao.StoreDAO;
import pachinko.dao.StoreModelDAO;
import pachinko.util.DBUtil;

@WebServlet("/insertStore")
public class InsertStoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String modelName = request.getParameter("model_name");

        // 入力が空の場合はエラーメッセージを表示
        if (storeName == null || storeName.isEmpty() || modelName == null || modelName.isEmpty()) {
            request.setAttribute("errorMessage", "店舗名または機種名が未入力です。");
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
            return;
        }

        // データベース接続を開く
        try (Connection con = DBUtil.getConnection()) {
            StoreDAO storeDAO = new StoreDAO();
            ModelDAO modelDAO = new ModelDAO();

            // 店舗IDを取得
            int storeId = storeDAO.getStoreIdByName(con, storeName);
            boolean isNewStore = false;
            if (storeId == -1) { // 店舗が存在しない場合は新規登録
                storeDAO.insertStore(con, storeName);
                storeId = storeDAO.getStoreIdByName(con, storeName);
                isNewStore = true; // 新規店舗フラグ
            }

            // 機種IDを取得
            int modelId = modelDAO.getModelIdByName(con, modelName);
            boolean isNewModel = false;
            if (modelId == -1) { // 機種が存在しない場合は新規登録
                modelDAO.insertModel(con, modelName);
                modelId = modelDAO.getModelIdByName(con, modelName);
                isNewModel = true; // 新規機種フラグ
            }

            // 既存の店舗と機種が両方登録されている場合
            StoreModelDAO storeModelDAO = new StoreModelDAO();
            boolean isDuplicate = storeModelDAO.isStoreModelDuplicate(con, storeId, modelId);
            if (isDuplicate) {
                request.setAttribute("errorMessage", "指定された店舗と機種は既に登録されています。");
                request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
                return;
            }

            // 店舗と機種の紐付けを登録
            storeModelDAO.insertStoreModel(con, storeId, modelId);

            // メッセージの設定
            if (isNewStore && isNewModel) {
                request.setAttribute("successMessage", "新規店舗と新規機種が登録されました。");
            } else if (!isNewStore && isNewModel) {
                request.setAttribute("successMessage", "既存店舗に新規機種が追加されました。");
            } else if (isNewStore && !isNewModel) {
                request.setAttribute("successMessage", "新規店舗に既存機種が追加されました。");
            }

            // 結果表示ページにフォワード
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
        } catch (SQLException e) {
            // エラー詳細をログに出力
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
        } catch (Exception e) {
            // その他の例外もキャッチしてエラーメッセージを表示
            e.printStackTrace();
            request.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
        }
    }
}