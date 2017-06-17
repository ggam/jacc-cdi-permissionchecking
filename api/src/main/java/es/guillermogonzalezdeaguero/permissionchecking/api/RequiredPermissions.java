package es.guillermogonzalezdeaguero.permissionchecking.api;

import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author Guillermo González de Agüero
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface RequiredPermissions {

    String[] value();
}
