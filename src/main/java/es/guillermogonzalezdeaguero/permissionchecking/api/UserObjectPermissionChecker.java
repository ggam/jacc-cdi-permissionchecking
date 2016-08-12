package es.guillermogonzalezdeaguero.permissionchecking.api;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
public interface UserObjectPermissionChecker<T> {

    boolean checkPermission(ObjectPermission<T> permission);
}
