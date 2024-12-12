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
        try (Connection conn = DBUtil.getConnection()) {
            String storeName = request.getParameter("store_name");
            if (storeName == null || storeName.trim().isEmpty()) {
                request.setAttribute("message", "店舗名が選択されていません。");
                request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
                return;
            }

            // storeId を取得してから店舗に関連するモデルを取得
            int storeId = storeDAO.getStoreIdByName(conn, storeName);
            if (storeId == -1) {
                request.setAttribute("message", "指定された店舗が存在しません。店舗名: " + storeName);
                request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
                return;
            }

            List<String> modelNames = modelDAO.getModelsByStoreName(storeName);  // 店舗名を渡す
            request.setAttribute("storeName", storeName);  // storeName をセット
            request.setAttribute("modelNames", modelNames);

            request.getRequestDispatcher("insertData.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データ取得中にエラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String storeName = request.getParameter("store_name");
        String modelName = request.getParameter("model_name");
        String dataDate = request.getParameter("data_date").trim();
        String pachinkoData = request.getParameter("pachinko_data").trim();

        java.sql.Date sqlDate = convertToSqlDate(dataDate);
        if (sqlDate == null) {
            request.setAttribute("message", "日付の形式が正しくありません。");
            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
            return;
        }

        if (storeName == null || storeName.trim().isEmpty() || modelName == null || modelName.trim().isEmpty()) {
            request.setAttribute("message", "店舗名または機種名が正しくありません。");
            request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            int storeId = getIdByName(con, "stores", "store_name", storeName);
            int modelId = getIdByName(con, "model_list", "model_name", modelName);

            // 店舗IDと機種IDが無効な場合
            if (storeId == -1 || modelId == -1) {
                request.setAttribute("message", "指定された店舗または機種名がデータベースに存在しません。店舗名: " + storeName + ", 機種名: " + modelName);
                request.getRequestDispatcher("insertDataResult.jsp").forward(request, response);
                return;
            }

            StoreModelDAO storeModelDAO = new StoreModelDAO();
            if (!storeModelDAO.isStoreModelDuplicate(con, storeId, modelId)) {
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
                    int machineNumber = Integer.parseInt(cols[0].replace(",", ""));
                    int totalGames = Integer.parseInt(cols[1].replace(",", ""));
                    int diffMedals = Integer.parseInt(cols[2].replace(",", ""));
                    int bb = Integer.parseInt(cols[3].replace(",", ""));
                    int rb = Integer.parseInt(cols[4].replace(",", ""));
                    double combinedProb = Double.parseDouble(cols[5].replace("1/", ""));
                    double bbProb = Double.parseDouble(cols[6].replace("1/", ""));
                    double rbProb = Double.parseDouble(cols[7].replace("1/", ""));

                    if (!storeDataDAO.isDataDuplicate(con, storeId, modelId, sqlDate, machineNumber)) {
                        boolean success = storeDataDAO.insertStoreData(con, storeId, modelId, sqlDate,
                                machineNumber, totalGames, diffMedals, bb, rb, combinedProb, bbProb, rbProb);

                        if (!success) {
                            isSuccess = false;
                            failedEntries.add("台番号: " + machineNumber + "（データ登録に失敗）");
                        }
                    } else {
                        isSuccess = false;
                        failedEntries.add("台番号: " + machineNumber + "（データが既に存在）");
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    failedEntries.add("台番号: " + (cols.length > 0 ? cols[0] : "不明") + "（データ形式エラー）");
                    e.printStackTrace();
                }
            }

            if (isSuccess) {
                request.setAttribute("message", "すべてのデータを正常に登録しました。");
            } else {
                if (!failedEntries.isEmpty()) {
                    String failedMachines = String.join(", ", failedEntries);
                    request.setAttribute("message", "一部のデータ登録に失敗しました: " + failedMachines);
                } else {
                    request.setAttribute("message", "一部のデータ登録に失敗しましたが、失敗した台番号は特定できませんでした。");
                }
            }

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

    private java.sql.Date convertToSqlDate(String dateString) {
        java.sql.Date date = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateString);
            date = new java.sql.Date(utilDate.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

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
