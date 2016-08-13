package es.guillermogonzalezdeaguero.permissionchecking;

import es.guillermogonzalezdeaguero.permissionchecking.api.ObjectPermission;
import es.guillermogonzalezdeaguero.permissionchecking.api.UserObjectPermissionChecker;
import java.security.Principal;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
public class PostPermissionChecker implements UserObjectPermissionChecker<Post> {

    // SHOULD NOT DO THIS!!
    @Inject
    private HttpServletRequest httpRequest;

    @Override
    public boolean checkPermission(ObjectPermission<Post> permission) {
        if (httpRequest.isUserInRole("admin")) {
            // Administratos can do anything they want
            return true;
        }

        switch (permission.getActions()) {
            case PostPermissions.DELETE:
                // Only admins may delete posts
                return false;
            case PostPermissions.UPDATE:
                Principal userPrincipal = httpRequest.getUserPrincipal();
                if (userPrincipal != null && Objects.equals(userPrincipal.getName(), permission.getObject().getAuthor())) {
                    return true;
                }

                // Additional checks here: is the user a moderator of the category this post belongs to?
                return false;
        }

        // In any other case, return false just to prevent security holes
        return false;
    }

}
