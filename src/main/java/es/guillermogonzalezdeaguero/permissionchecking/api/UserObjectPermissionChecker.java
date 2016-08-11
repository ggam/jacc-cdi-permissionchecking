package es.guillermogonzalezdeaguero.permissionchecking.api;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
public interface UserObjectPermissionChecker {

    boolean checkPermission(ObjectPermission permission);
}
