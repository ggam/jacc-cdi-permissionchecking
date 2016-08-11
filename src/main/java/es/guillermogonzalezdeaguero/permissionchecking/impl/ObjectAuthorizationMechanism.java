package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.ObjectPermission;
import es.guillermogonzalezdeaguero.permissionchecking.api.UserObjectPermissionChecker;
import java.security.Permission;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.omnifaces.jaccprovider.cdi.AuthorizationMechanism;
import org.omnifaces.jaccprovider.jacc.Caller;
import org.omnifaces.jaccprovider.jacc.SecurityConstraints;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
public class ObjectAuthorizationMechanism implements AuthorizationMechanism {

    @Inject
    private Instance<UserObjectPermissionChecker> permissionCheckers;

    @Override
    public Boolean postAuthenticatePreAuthorize(Permission requestedPermission, Caller caller, SecurityConstraints securityConstraints) {
        return doChecks(requestedPermission);
    }

    @Override
    public Boolean preAuthenticatePreAuthorize(Permission requestedPermission, SecurityConstraints securityConstraints) {
        return doChecks(requestedPermission);
    }

    private Boolean doChecks(Permission requestedPermission) {
        ObjectPermission ourPermission;
        if (!(requestedPermission instanceof ObjectPermission)) {
            return null;
        }

        ourPermission = (ObjectPermission) requestedPermission;

        for (UserObjectPermissionChecker permissionChecker : permissionCheckers) {
            if (!permissionChecker.checkPermission(ourPermission)) {
                return false;
            }
        }

        return true;
    }

}
