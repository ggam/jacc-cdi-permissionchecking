package es.guillermogonzalezdeaguero.permissionchecking.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * @author Guillermo González de Agüero
 */
@Inherited
@Retention(RUNTIME)
@Target({METHOD})
public @interface PermissionEvaluator {
}
