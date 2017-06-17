package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine;

import java.lang.reflect.Method;
import java.util.Collection;
import javax.enterprise.inject.Vetoed;

/**
 *
 * @author Guillermo González de Agüero
 */
@Vetoed
public class Engine {

    private Collection<Method> methods;

    public Engine() {
    }

    public Engine(Collection<Method> methods) {
        this.methods = methods;
    }

    public Expression parseExpression(String expressionValue) throws InvalidExpressionException {
        try {
            String[] split = expressionValue.split("\\("); // ["checkPermission", "arg0, arg01)"]
            String methodName = split[0]; // "checkPermission"
            String[] split1 = split[1].split("\\)"); // ["arg0, arg1"]
            String[] arguments = split1[0].split(","); // ["arg0", "arg1"]

            Method method = methods.
                    stream().
                    filter(m -> m.getName().equals(methodName)).
                    findAny().
                    get();

            if (method.getParameterCount() != arguments.length) {
                throw new IllegalArgumentException("Invalid parameter count");
            }

            return new Expression(method.getDeclaringClass(), method);
        } catch (Exception e) {
            throw new InvalidExpressionException("Invalid Expression: " + expressionValue, e);
        }
    }
}
