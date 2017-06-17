package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;

/**
 *
 * @author Guillermo González de Agüero
 */
@Vetoed
public class Expression {

    private Class<?> beanClass;
    private Method method;

    public Expression(Class<?> cdiBeanClass, Method method) {
        this.beanClass = cdiBeanClass;
        this.method = method;
    }

    public boolean eval(Object... parameters) {
        try {
            return (boolean) method.invoke(CDI.current().select(beanClass).get(), parameters);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Evalution exception", e);
        }
    }
}
