package es.guillermogonzalezdeaguero.permissionchecking;

import java.io.IOException;
import javax.annotation.security.DeclareRoles;
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
@DeclareRoles("admin")
public class TestServlet extends HttpServlet {

    @Inject
    private PostsRepository postsRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        postsRepository.customPermission(req.getParameter("previewCheck"));

        Integer postId = req.getParameter("postId") != null ? Integer.valueOf(req.getParameter("postId")) : null;
        String action = req.getParameter("action");

        if (postId == null || action == null) {
            throw new ServletException("Both \"postId\" and \"action\" parameters are mandatory");
        }

        Post post = postsRepository.findById(postId).get();
        switch (action) {
            case PostPermissions.DELETE:
                postsRepository.delete(post);
                break;
            case PostPermissions.UPDATE:
                postsRepository.update(post);
                break;
            default:
                throw new ServletException("Valid actions are \"" + PostPermissions.DELETE + "\" and \"" + PostPermissions.UPDATE + "\"");
        }

        resp.getWriter().println("Succesfully " + action + "d post #" + postId + " by user " + req.getUserPrincipal().getName() + "!!");
    }

}
