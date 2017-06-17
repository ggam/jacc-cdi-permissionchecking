package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation;

import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import static javax.interceptor.Interceptor.Priority.APPLICATION;
import javax.interceptor.InvocationContext;
import es.guillermogonzalezdeaguero.permissionchecking.api.AuthorizationException;
import es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine.Engine;
import es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine.Expression;
import es.guillermogonzalezdeaguero.permissionchecking.api.Allowed;

/**
 *
 * @author Guillermo González de Agüero
 */
@Interceptor
@Priority(APPLICATION)
@Allowed("")
public class PermissionEvaluationInterceptor {

    private static final Logger LOGGER = Logger.getLogger(PermissionEvaluationInterceptor.class.getName());

    @Inject
    private Engine engine;

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        Allowed annotation = ic.getMethod().getAnnotation(Allowed.class);
        String expression = annotation.value(); // "checkPermission(arg0, arg1)"

        Expression parseExpression = engine.parseExpression(expression);
        if (!parseExpression.eval(ic.getParameters())) {
            throw new AuthorizationException("AUTH CHECKS failed");
        }

        return ic.proceed();
    }

}
