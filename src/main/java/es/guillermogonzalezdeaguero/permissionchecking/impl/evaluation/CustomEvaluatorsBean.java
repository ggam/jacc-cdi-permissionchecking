package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author Guillermo González de Agüero
 */
public class CustomEvaluatorsBean {

    private Map<String, Method> methods;

    public CustomEvaluatorsBean() {
    }

    public CustomEvaluatorsBean(Map<String, Method> methods) {
        this.methods = methods;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

}
