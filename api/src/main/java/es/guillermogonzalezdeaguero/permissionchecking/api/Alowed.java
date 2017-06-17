package es.guillermogonzalezdeaguero.permissionchecking.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author Guillermo González de Agüero
 */
@InterceptorBinding
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface Alowed {

    @Nonbinding // TODO: check if needed
    String value();
}
