package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine;

/**
 *
 * @author Guillermo González de Agüero
 */
public class InvalidExpressionException extends Exception {

    public InvalidExpressionException(String message, Throwable e) {
        super(message, e);
    }

}
