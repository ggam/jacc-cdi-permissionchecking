package es.guillermogonzalezdeaguero.permissionchecking;

import es.guillermogonzalezdeaguero.permissionchecking.api.ObjectPermission;
import es.guillermogonzalezdeaguero.permissionchecking.api.UserObjectPermissionChecker;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
public class ProductPermissionChecker implements UserObjectPermissionChecker {

    @Override
    public boolean checkPermission(ObjectPermission permission) {
        if (!(permission.getObject() instanceof ProductEntity)) {
            return true;
        }

        // Some application specific logic here to check if the caller has the required permissions (querying a database, etc.)
        if ("edit".equals(permission.getActions())) {
            return true;
        }

        return false;
    }

}
