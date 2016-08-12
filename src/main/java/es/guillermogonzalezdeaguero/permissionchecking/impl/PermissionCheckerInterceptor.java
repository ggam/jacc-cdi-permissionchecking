package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.EnablePermissionChecking;
import es.guillermogonzalezdeaguero.permissionchecking.api.ObjectPermission;
import es.guillermogonzalezdeaguero.permissionchecking.api.RequiredPermissions;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import static javax.interceptor.Interceptor.Priority.APPLICATION;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Guillermo González de Agüero
 */
@Interceptor
@Priority(APPLICATION)
@EnablePermissionChecking
public class PermissionCheckerInterceptor {

    @Inject
    private ObjectAuthorizationMechanism authorizationMechanism;

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        Method method = invocationContext.getMethod();
        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            RequiredPermissions annotation = parameter.getAnnotation(RequiredPermissions.class);
            if (annotation != null) {
                Object parameterValue = invocationContext.getParameters()[i];

                for (String permissionToCheck : annotation.value()) {
                    // SHOULD USE JACC HERE
                    //if (Boolean.FALSE.equals(Policy.getPolicy().implies(new ProtectionDomain(null, null), new ObjectPermission(parameterValue, permissionToCheck)))) {
                    if (Boolean.FALSE.equals(authorizationMechanism.doChecks(new ObjectPermission(parameterValue, permissionToCheck)))) {
                        throw new SecurityException("You are NOT allowed to do that");
                    }
                }
            }
            i++;
        }

        return invocationContext.proceed();
    }
}
