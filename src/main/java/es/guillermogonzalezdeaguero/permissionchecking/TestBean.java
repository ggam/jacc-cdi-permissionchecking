package es.guillermogonzalezdeaguero.permissionchecking;

import es.guillermogonzalezdeaguero.permissionchecking.api.EnablePermissionChecking;
import es.guillermogonzalezdeaguero.permissionchecking.api.RequiredPermissions;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
@EnablePermissionChecking
public class TestBean {

    public void edit(@RequiredPermissions("edit") ProductEntity product) {
        System.out.println("Product was updated!");
    }
}
