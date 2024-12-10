package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    private StoreDAO storeDAO;
    private ModelDAO modelDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        storeDAO = new StoreDAO();
        modelDAO = new ModelDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String modelName1 = request.getParameter("model_name1");
        String modelName2 = request.getParameter("model_name2");
        String modelName3 = request.getParameter("model_name3");

        // 空白を削除
        if (storeName != null) storeName = storeName.trim();
        if (modelName1 != null) modelName1 = modelName1.trim();
        if (modelName2 != null) modelName2 = modelName2.trim();
        if (modelName3 != null) modelName3 = modelName3.trim();

        // 入力チェック
        if (storeName == null || storeName.isEmpty()) {
            request.setAttribute("errorMessage", "店舗名が未入力です。");
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
            return;
        }

        // 有効な機種名だけをリストに追加
        List<String> validModelNames = new ArrayList<>();
        if (modelName1 != null && !modelName1.isEmpty()) validModelNames.add(modelName1);
        if (modelName2 != null && !modelName2.isEmpty()) validModelNames.add(modelName2);
        if (modelName3 != null && !modelName3.isEmpty()) validModelNames.add(modelName3);

        if (validModelNames.isEmpty()) {
            request.setAttribute("errorMessage", "少なくとも1つの機種名を入力してください。");
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            // 店舗IDを取得または登録
            int storeId = storeDAO.getStoreIdByName(con, storeName);
            if (storeId == -1) {
                storeDAO.insertStore(con, storeName);
                storeId = storeDAO.getStoreIdByName(con, storeName);
            }

            // 機種ごとの登録処理
            StoreModelDAO storeModelDAO = new StoreModelDAO();
            List<String> duplicateModels = new ArrayList<>();
            List<String> newModels = new ArrayList<>();

            for (String modelName : validModelNames) {
                int modelId = modelDAO.getModelIdByName(con, modelName);
                if (modelId == -1) {
                    modelDAO.insertModel(con, modelName);
                    modelId = modelDAO.getModelIdByName(con, modelName);
                }

                if (storeModelDAO.isStoreModelDuplicate(con, storeId, modelId)) {
                    duplicateModels.add(modelName);
                } else {
                    storeModelDAO.insertStoreModel(con, storeId, modelId);
                    newModels.add(modelName);
                }
            }

            // 結果メッセージ設定
            StringBuilder message = new StringBuilder();
            if (!newModels.isEmpty()) {
                message.append("以下の機種が登録されました: ").append(String.join(", ", newModels)).append(".<br>");
            }
            if (!duplicateModels.isEmpty()) {
                message.append("既に登録されている機種:<br>").append(String.join(", ", duplicateModels)).append(".");
            }

            if (message.length() > 0) {
                request.setAttribute("successMessage", message.toString());
            } else {
                request.setAttribute("errorMessage", "すべての機種が既に登録済みです。");
            }

            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            request.getRequestDispatcher("/insertStoreResult.jsp").forward(request, response);
        }
    }
}
