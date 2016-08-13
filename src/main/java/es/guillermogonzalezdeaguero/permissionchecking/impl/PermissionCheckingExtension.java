package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.CheckPermissions;
import es.guillermogonzalezdeaguero.permissionchecking.api.RequiredPermissions;
import es.guillermogonzalezdeaguero.permissionchecking.impl.cdi.CheckedPermissionsAnnotatedMethod;
import es.guillermogonzalezdeaguero.permissionchecking.impl.cdi.CheckedPermissionsAnnotatedType;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

/**
 *
 * @author Guillermo González de Agüero
 */
public class PermissionCheckingExtension<X> implements Extension {

    private static final Logger LOGGER = Logger.getLogger(PermissionCheckingExtension.class.getName());

    public <X> void processAnnotatedType(@Observes @WithAnnotations(RequiredPermissions.class) ProcessAnnotatedType<X> pat) {
        AnnotatedType<X> annotatedType = pat.getAnnotatedType();

        if (annotatedType.isAnnotationPresent(RequiredPermissions.class)) {
            // Annotation is present at class level. No need to put it at method level
            return;
        }

        Set<AnnotatedMethod<? super X>> newAnnotatedMethods = new HashSet<>();

        // Only do the checks if class does not already has the interceptor binding
        for (AnnotatedMethod<? super X> method : annotatedType.getMethods()) {
            if (!method.isAnnotationPresent(CheckPermissions.class)) {
                for (AnnotatedParameter<? super X> parameter : method.getParameters()) {
                    if (parameter.getAnnotation(RequiredPermissions.class) != null) {
                        // This method requires permission checking
                        newAnnotatedMethods.add(new CheckedPermissionsAnnotatedMethod<>(method));

                        LOGGER.log(Level.INFO, "Added PermissionChecking interceptor binding to {0}#{1}", new String[]{annotatedType.getJavaClass().getName(), method.getJavaMember().getName()});
                    } else {
                        newAnnotatedMethods.add(method);
                    }
                }
            }
        }

        AnnotatedType<X> wrappedAnnotated = new CheckedPermissionsAnnotatedType<>(annotatedType, newAnnotatedMethods);
        pat.setAnnotatedType(wrappedAnnotated);

    }

}
