package pachinko;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pachinko.dao.ModelDAO;

@WebServlet("/viewData")
public class ViewDataServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("store_name");

        // store_name をセッションに保存
        HttpSession session = request.getSession();
        session.setAttribute("storeName", storeName);

        // 必要に応じてDAOを呼び出し、機種一覧を取得
        ModelDAO modelDAO = new ModelDAO();
        List<String> modelNames = modelDAO.getModelsByStoreName(storeName);
        
        if (modelNames == null || modelNames.isEmpty()) {
            request.setAttribute("errorMessage", "選択可能な機種がありません。");
        }

        // リクエスト属性に設定してJSPに渡す
        request.setAttribute("storeName", storeName);
        request.setAttribute("modelNames", modelNames);

        // JSP にフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewData.jsp");
        dispatcher.forward(request, response);
    }
}
