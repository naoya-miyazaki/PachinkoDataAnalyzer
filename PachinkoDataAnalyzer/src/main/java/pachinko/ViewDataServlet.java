package pachinko;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import pachinko.dao.StoreDataDAO;

public class ViewDataServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");
        String dataDate = request.getParameter("data_date");
        String modelName = request.getParameter("model_name");
        
        StoreDataDAO storeDataDAO = new StoreDataDAO();
        List<StoreData> storeDataList;
        
        if ("all".equals(modelName)) {
            storeDataList = storeDataDAO.getDataByStoreAndDate(storeName, dataDate);
        } else {
            storeDataList = storeDataDAO.getDataByStoreDateAndModel(storeName, dataDate, modelName);
        }
        
        request.setAttribute("storeName", storeName);
        request.setAttribute("dataDate", dataDate);
        request.setAttribute("modelName", "all".equals(modelName) ? "すべての機種" : modelName);
        request.setAttribute("storeDataList", storeDataList);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewDataResult.jsp");
        dispatcher.forward(request, response);
    }
}
