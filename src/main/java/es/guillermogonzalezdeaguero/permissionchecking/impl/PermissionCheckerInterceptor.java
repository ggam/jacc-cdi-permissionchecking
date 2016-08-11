package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.EnablePermissionChecking;
import es.guillermogonzalezdeaguero.permissionchecking.api.ObjectPermission;
import es.guillermogonzalezdeaguero.permissionchecking.api.RequiredPermissions;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.Policy;
import java.security.ProtectionDomain;
import javax.annotation.Priority;
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

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        Method method = invocationContext.getMethod();
        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            RequiredPermissions annotation = parameter.getAnnotation(RequiredPermissions.class);
            if (annotation != null) {
                Object parameterValue = invocationContext.getParameters()[i];

                for (String permissionToCheck : annotation.value()) {
                    if (!Policy.getPolicy().implies(new ProtectionDomain(null, null), new ObjectPermission(parameterValue, permissionToCheck))) {
                        throw new SecurityException("You are NOT allowed to do that");
                    }
                }
            }
            i++;
        }

        return invocationContext.proceed();
    }
}
