package es.guillermogonzalezdeaguero.permissionchecking;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Guillermo González de Agüero
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {

    @Inject
    private TestBean bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        bean.edit(new ProductEntity());
        resp.getWriter().print("<h1>It works!</h1>");
    }

}
