package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.EnablePermissionChecking;
import es.guillermogonzalezdeaguero.permissionchecking.api.RequiredPermissions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 *
 * @author Guillermo González de Agüero
 */
public class PermissionCheckingExtension<X> implements Extension {

    private static final Logger LOGGER = Logger.getLogger(PermissionCheckingExtension.class.getName());

    public <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> pat) {
        AnnotatedType<X> annotatedType = pat.getAnnotatedType();

        // Only do the checks if class does not already has the interceptor binding
        if (annotatedType.getAnnotation(EnablePermissionChecking.class) == null) {
            for (AnnotatedMethod<? super X> method : pat.getAnnotatedType().getMethods()) {
                for (AnnotatedParameter<? super X> parameter : method.getParameters()) {
                    if (parameter.getAnnotation(RequiredPermissions.class) != null) {
                        // This method required the interceptor. Enable on at class level and go out
                        AnnotatedType<X> wrappedAnnotated = new WrappedAnnotated<>(annotatedType);
                        pat.setAnnotatedType(wrappedAnnotated);
                        LOGGER.log(Level.FINE, "Added PermissionChecking interceptor binding to {0}", pat.getAnnotatedType().getJavaClass().getName());
                        return;
                    }
                }
            }
        }
    }

    class WrappedAnnotated<X> implements AnnotatedType<X> {

        private AnnotatedType wrapped;
        private final Set<Annotation> annotations;
        private final Annotation permissionCheckingAnnotation;

        public WrappedAnnotated(AnnotatedType wrapped) {
            this.wrapped = wrapped;
            permissionCheckingAnnotation = new EnablePermissionChecking() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return EnablePermissionChecking.class;
                }
            };

            annotations = new HashSet<>(wrapped.getAnnotations());
            annotations.add(permissionCheckingAnnotation);
        }

        @Override
        public Class<X> getJavaClass() {
            return wrapped.getJavaClass();
        }

        @Override
        public Set<AnnotatedConstructor<X>> getConstructors() {
            return wrapped.getConstructors();
        }

        @Override
        public Set<AnnotatedMethod<? super X>> getMethods() {
            return wrapped.getMethods();
        }

        @Override
        public Set<AnnotatedField<? super X>> getFields() {
            return wrapped.getFields();
        }

        @Override
        public Type getBaseType() {
            return wrapped.getBaseType();
        }

        @Override
        public Set<Type> getTypeClosure() {
            return wrapped.getTypeClosure();
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class< T> annotationType) {
            if (EnablePermissionChecking.class.equals(annotationType)) {
                return (T) permissionCheckingAnnotation;
            }

            return wrapped.getAnnotation(annotationType);
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return annotations;
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            if (EnablePermissionChecking.class.equals(annotationType)) {
                return true;
            }

            return wrapped.isAnnotationPresent(annotationType);
        }

    }
}
