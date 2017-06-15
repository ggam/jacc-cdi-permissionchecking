package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation;

import es.guillermogonzalezdeaguero.permissionchecking.api.AuthorizationException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.el.ELProcessor;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import static javax.interceptor.Interceptor.Priority.APPLICATION;
import javax.interceptor.InvocationContext;
import es.guillermogonzalezdeaguero.permissionchecking.api.Alowed;

/**
 *
 * @author Guillermo González de Agüero
 */
@Interceptor
@Priority(APPLICATION)
@Alowed("")
public class PermissionEvaluationInterceptor {

    private static final Logger LOGGER = Logger.getLogger(PermissionEvaluationInterceptor.class.getName());

    @Inject
    private CustomEvaluatorsBean customEvaluatorsBean;

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {

        LOGGER.info("Intercepted by " + getClass().getName());

        Alowed annotation = ic.getMethod().getAnnotation(Alowed.class);
        String value = annotation.value();

        ELProcessor elProcessor = new ELProcessor();
        int i = 0;
        for (Object parameter : ic.getParameters()) {
            elProcessor.defineBean("arg" + i, parameter);
            i++;
        }

        for (Map.Entry<String, Method> entry : customEvaluatorsBean.getMethods().entrySet()) {
            elProcessor.defineFunction("", entry.getKey(), entry.getValue());
        }

        boolean result = (boolean) elProcessor.eval(value);

        if (!result) {
            throw new AuthorizationException("AUTH CHECKS failed");
        }

        return ic.proceed();
    }
}
