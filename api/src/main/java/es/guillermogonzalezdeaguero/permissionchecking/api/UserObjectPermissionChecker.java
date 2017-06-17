package es.guillermogonzalezdeaguero.permissionchecking.api;

/**
 *
 * @author Guillermo González de Agüero
 */
public interface UserObjectPermissionChecker<T> {

    boolean checkPermission(ObjectPermission<T> permission);
}
