package pachinko;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import pachinko.dao.StoreDataDAO;
import pachinko.model.StoreData;

@WebServlet("/viewDataResult")
public class ViewDataResultServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String modelName = request.getParameter("model_name");
        String date = request.getParameter("data_date");

        // "all" が選ばれた場合、モデル名を null に設定
        if ("all".equals(modelName)) {
            modelName = null;
        }

        StoreDataDAO storeDataDAO = new StoreDataDAO();
        List<StoreData> dataList = storeDataDAO.getDataByStoreAndModelAndDate(storeName, modelName, date);

        request.setAttribute("storeName", storeName);
        request.setAttribute("modelName", modelName);
        request.setAttribute("dataList", dataList);
        
        if (dataList == null || dataList.isEmpty()) {
            request.setAttribute("errorMessage", "該当するデータはありません。");
            request.getRequestDispatcher("noData.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("viewDataResult.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String modelName = request.getParameter("model_name");
        String date = request.getParameter("data_date");

        // "all" が選ばれた場合、モデル名を null に設定
        if ("all".equals(modelName)) {
            modelName = null;
        }
        

        StoreDataDAO storeDataDAO = new StoreDataDAO();
        List<StoreData> storeDataList = storeDataDAO.getDataByStoreAndModelAndDate(storeName, modelName, date);

       
       
        if (!storeDataList.isEmpty()) {
            Map<String, List<StoreData>> groupedData = storeDataList.stream()
                .filter(data -> data.getModelName() != null) // null を除外
                .collect(Collectors.groupingBy(StoreData::getModelName));
            request.setAttribute("groupedData", groupedData);
        }
   
        // リクエスト属性に設定してJSPに渡す
        request.setAttribute("storeName", storeName);
        request.setAttribute("modelName", modelName);
        request.setAttribute("date", date);
        request.setAttribute("storeDataList", storeDataList);
        


        // JSPにフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewDataResult.jsp");
        dispatcher.forward(request, response);
    }
}
