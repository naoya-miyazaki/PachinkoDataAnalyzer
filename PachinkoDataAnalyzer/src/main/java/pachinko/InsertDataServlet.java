package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import pachinko.dao.StoreDataDAO;
import pachinko.dao.StoreModelDAO;
import pachinko.util.DBUtil;

@WebServlet("/insertData")
public class InsertDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StoreDAO storeDAO;
    private ModelDAO modelDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        storeDAO = new StoreDAO();
        modelDAO = new ModelDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (Connection con = DBUtil.getConnection()) {
            // 店舗名のリストを取得
            List<String> storeNames = storeDAO.getStoreNames(con);
            
            // 機種名のリストを取得
            List<String> modelNames = modelDAO.getModelNames(con);

            // 店舗名と機種名のリストをリクエストスコープに設定
            request.setAttribute("storeNames", storeNames);
            request.setAttribute("modelNames", modelNames);

            // insertData.jspにフォワード
            request.getRequestDispatcher("/insertData.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // フォームからのパラメータ取得
        String storeName = request.getParameter("store_name");
        String modelName = request.getParameter("model_name");
        String dataDate = request.getParameter("data_date").trim();
        String pachinkoData = request.getParameter("pachinko_data").trim();

        // 文字列の日付をjava.sql.Dateに変換
        java.sql.Date sqlDate = convertToSqlDate(dataDate);
        if (sqlDate == null) {
            request.setAttribute("message", "日付の形式が正しくありません。");
            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            // 店舗IDと機種IDを取得
            int storeId = getIdByName(con, "stores", "store_name", storeName);
            int modelId = getIdByName(con, "model_list", "model_name", modelName);

            // 店舗IDと機種IDが無効な場合
            if (storeId == -1 || modelId == -1) {
                request.setAttribute("message", "店舗または機種名が正しくありません。");
                request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
                return;
            }

            // 店舗と機種の関連チェック（重複防止）
            StoreModelDAO storeModelDAO = new StoreModelDAO();
            if (!storeModelDAO.isStoreModelDuplicate(con, storeId, modelId)) {
                // 重複していない店舗と機種の組み合わせを登録
                storeModelDAO.insertStoreModel(con, storeId, modelId);
            }

            List<String> failedEntries = new ArrayList<>();
            StoreDataDAO storeDataDAO = new StoreDataDAO();
            boolean isSuccess = true;

            String[] rows = pachinkoData.split("\n");
            for (String row : rows) {
                String[] cols = row.split("\\s+");
                if (cols.length != 8) {
                    isSuccess = false;
                    failedEntries.add("台番号: " + (cols.length > 0 ? cols[0] : "不明") + "（データ形式が不正）");
                    continue;
                }
                try {
                    // 入力データのパース
                    int machineNumber = Integer.parseInt(cols[0].replace(",", ""));
                    int totalGames = Integer.parseInt(cols[1].replace(",", ""));
                    int diffMedals = Integer.parseInt(cols[2].replace(",", ""));
                    int bb = Integer.parseInt(cols[3].replace(",", ""));
                    int rb = Integer.parseInt(cols[4].replace(",", ""));
                    double combinedProb = Double.parseDouble(cols[5].replace("1/", ""));
                    double bbProb = Double.parseDouble(cols[6].replace("1/", ""));
                    double rbProb = Double.parseDouble(cols[7].replace("1/", ""));

                    // 台データが既に存在するか確認
                    if (!storeDataDAO.isDataDuplicate(con, storeId, modelId, sqlDate, machineNumber)) {
                        // 重複していなければ新規登録
                        boolean success = storeDataDAO.insertStoreData(con, storeId, modelId, sqlDate,
                                machineNumber, totalGames, diffMedals, bb, rb, combinedProb, bbProb, rbProb);

                        if (!success) {
                            isSuccess = false;
                            failedEntries.add("台番号: " + machineNumber + "（データ登録に失敗）");
                        }
                    } else {
                        isSuccess = false;
                        failedEntries.add(String.valueOf(machineNumber)); // 台番号のみを格納
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    failedEntries.add("台番号: " + (cols.length > 0 ? cols[0] : "不明"));
                    e.printStackTrace();
                }
            }

         // 結果メッセージの設定
            if (isSuccess) {
                request.setAttribute("message", "すべてのデータを正常に登録しました。");
            } else {
                if (!failedEntries.isEmpty()) {
                    // 台番号リストをカンマ区切りに結合
                    String failedMachines = String.join(", ", failedEntries);
                    // メッセージを構築
                    request.setAttribute("message", "一部のデータ登録に失敗しました: 台番号: " + failedMachines + "（データが既に存在）");
                } else {
                    request.setAttribute("message", "一部のデータ登録に失敗しましたが、失敗した台番号は特定できませんでした。");
                }
            }

            // 結果ページに遷移
            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);


            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "データベース接続に失敗しました。エラー: " + e.getMessage());
            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "データの登録に失敗しました。エラー: " + e.getMessage());
            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
        }
    }

    // 日付文字列をjava.sql.Dateに変換するメソッド
    private java.sql.Date convertToSqlDate(String dateString) {
        java.sql.Date date = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateString);  // StringからDateに変換
            date = new java.sql.Date(utilDate.getTime());    // DateからSQL Dateに変換
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // 名前からIDを取得するメソッド
    private int getIdByName(Connection con, String tableName, String columnName, String name) throws SQLException {
        int id = -1;
        String sql = "SELECT id FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        }
        return id;
    }
}

