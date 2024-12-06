//package pachinko;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//import javax.naming.NamingException;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import dao.ModelDAO;
//import dao.StoreDAO;
//import dao.StoreDataDAO;
//
//@WebServlet("/insertData")
//public class InsertDataServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    private StoreDAO storeDAO = new StoreDAO();
//    private ModelDAO modelDAO = new ModelDAO();
//    private StoreDataDAO storeDataDAO = new StoreDataDAO();
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // 入力パラメータの取得
//        String storeName = request.getParameter("store_name");
//        String dataDate = request.getParameter("data_date");
//        String modelName = request.getParameter("model_name"); // 機種名
//        String pachinkoData = request.getParameter("pachinko_data");
//
//        // 文字エンコーディングの設定
//        request.setCharacterEncoding("UTF-8");  
//        response.setCharacterEncoding("UTF-8"); 
//        response.setContentType("text/html; charset=UTF-8");
//
//        // 必須項目チェック
//        if (storeName == null || storeName.isEmpty() || 
//            dataDate == null || dataDate.isEmpty() || 
//            modelName == null || modelName.isEmpty() || 
//            pachinkoData == null || pachinkoData.isEmpty()) {
//            response.getWriter().println("店舗名、日付、機種名、台データは必須です。");
//            return;
//        }
//
//        // 重複チェック
//        try {
//            boolean isDuplicate = storeDataDAO.checkDuplicate(storeName, dataDate, modelName);
//            if (isDuplicate) {
//                response.sendRedirect("insertDataResult.jsp?result=duplicate");
//                return;
//            }
//
//            // 店舗名と機種名からIDを取得
//            int storeId = storeDAO.getStoreId(storeName);
//            int modelId = modelDAO.getModelId(modelName);
//
//            // 台データの格納
//            String[] rows = pachinkoData.split("\n");  // 改行で各行を分ける
//            for (String row : rows) {
//                String[] columns = row.split("\\s+");  // 空白やタブで分割
//
//                // 必要な項目がすべて揃っているかチェック
//                if (columns.length >= 8) {
//                    try {
//                        int machineNumber = Integer.parseInt(columns[0].replace(",", "").trim());  // 台番号
//                        int totalGames = Integer.parseInt(columns[1].replace(",", "").trim());     // G数
//                        int diffMedals = Integer.parseInt(columns[2].replace(",", "").trim());     // 差枚
//                        int bb = Integer.parseInt(columns[3].replace(",", "").trim());             // BB
//                        int rb = Integer.parseInt(columns[4].replace(",", "").trim());             // RB
//                        String combinedProb = columns[5].trim();  // 合成確率
//                        String bbProb = columns[6].trim();        // BB確率
//                        String rbProb = columns[7].trim();        // RB確率
//
//                        // 台データをstore_dataに格納
//                        storeDataDAO.insertMachineData(storeId, modelId, machineNumber, totalGames, diffMedals, bb, rb, combinedProb, bbProb, rbProb, dataDate);
//                    } catch (NumberFormatException e) {
//                        response.getWriter().println("無効なデータが含まれています。");
//                        return;
//                    }
//                }
//            }
//
//            // 登録が成功したことを結果画面に伝えるために、リダイレクト
//            response.sendRedirect("insertDataResult.jsp?result=success");
//        } catch (SQLException | NamingException e) {
//            throw new ServletException("データベースエラー: " + e.getMessage(), e);
//        }
//    }
//}
