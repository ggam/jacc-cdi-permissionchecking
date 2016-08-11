package es.guillermogonzalezdeaguero.permissionchecking.api;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author Guillermo González de Agüero
 */
@Stereotype
@InterceptorBinding
@Retention(RUNTIME)
@Target(TYPE)
public @interface EnablePermissionChecking {
}
