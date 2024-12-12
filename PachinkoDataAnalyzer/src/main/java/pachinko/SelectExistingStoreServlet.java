package pachinko;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import pachinko.dao.StoreDAO;
import pachinko.util.DBUtil;

@WebServlet("/selectExistingStore")
public class SelectExistingStoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StoreDAO storeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        storeDAO = new StoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection con = DBUtil.getConnection()) {
            // 店舗名のリストを取得
            List<String> storeNames = storeDAO.getStoreNames(con);

            // リクエストスコープに店舗名のリストをセット
            request.setAttribute("storeNames", storeNames);

            // selectExistingStore.jspにフォワード
            request.getRequestDispatcher("/selectExistingStore.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
