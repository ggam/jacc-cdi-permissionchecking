package es.guillermogonzalezdeaguero.permissionchecking.impl.permission.cdi;

import es.guillermogonzalezdeaguero.permissionchecking.api.CheckPermissions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author Guillermo González de Agüero
 */
public class CheckedPermissionsAnnotatedMethod<X> implements AnnotatedMethod<X> {

    private final AnnotatedMethod<X> wrapped;
    private final Annotation permissionCheckingAnnotation;

    public CheckedPermissionsAnnotatedMethod(AnnotatedMethod<X> wrapped) {
        this.wrapped = wrapped;
        permissionCheckingAnnotation = new AnnotationLiteral<CheckPermissions>() {
        };
    }

    @Override
    public Method getJavaMember() {
        return wrapped.getJavaMember();
    }

    @Override
    public List<AnnotatedParameter<X>> getParameters() {
        return wrapped.getParameters();
    }

    @Override
    public boolean isStatic() {
        return wrapped.isStatic();
    }

    @Override
    public AnnotatedType<X> getDeclaringType() {
        return wrapped.getDeclaringType();
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
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        if (CheckPermissions.class.equals(annotationType)) {
            return (T) permissionCheckingAnnotation;
        }
        return wrapped.getAnnotation(annotationType);
    }

    @Override
    public Set<Annotation> getAnnotations() {
        Set<Annotation> annotations = new HashSet<>(wrapped.getAnnotations());
        annotations.add(permissionCheckingAnnotation);
        return annotations;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        if (CheckPermissions.class.equals(annotationType)) {
            return true;
        }

        return wrapped.isAnnotationPresent(annotationType);
    }

}
