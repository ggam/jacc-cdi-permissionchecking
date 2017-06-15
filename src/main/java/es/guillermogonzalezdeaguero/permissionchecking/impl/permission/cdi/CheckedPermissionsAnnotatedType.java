package es.guillermogonzalezdeaguero.permissionchecking.impl.permission.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 *
 * @author Guillermo González de Agüero
 */
public class CheckedPermissionsAnnotatedType<X> implements AnnotatedType<X> {

    private AnnotatedType wrapped;
    private Set<AnnotatedMethod<? super X>> methods;

    public CheckedPermissionsAnnotatedType(AnnotatedType wrapped, Set<AnnotatedMethod<? super X>> methods) {
        this.wrapped = wrapped;
        this.methods = methods;
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
        return methods;
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
        return wrapped.getAnnotation(annotationType);
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return wrapped.getAnnotations();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return wrapped.isAnnotationPresent(annotationType);
    }

}
