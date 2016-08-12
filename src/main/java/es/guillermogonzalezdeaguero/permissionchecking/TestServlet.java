package es.guillermogonzalezdeaguero.permissionchecking;

import java.io.IOException;
import javax.annotation.security.DeclareRoles;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Guillermo González de Agüero
 */
@WebServlet("/test")
@DeclareRoles("admin")
@ServletSecurity(
        @HttpConstraint(rolesAllowed = "**"))
public class TestServlet extends HttpServlet {

    @Inject
    private PostsRepository postsRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer postId = req.getParameter("postId") != null ? Integer.valueOf(req.getParameter("postId")) : null;
        String action = req.getParameter("action");

        if (postId == null || action == null) {
            throw new ServletException("Both parameters \"postId\" and \"action\" are mandatory");
        }

        Post post = postsRepository.findById(postId).get();
        switch (action) {
            case "delete":
                postsRepository.delete(post);
                break;
            case "update":
                postsRepository.update(post);
                break;
            default:
                throw new ServletException("Valid actions are \"delete\" and \"update\"");
        }

        resp.getWriter().println("Succesfully " + action + "d post #" + postId + " by user " + req.getUserPrincipal().getName() + "!!");
    }

}
