package es.guillermogonzalezdeaguero.permissionchecking.api;

/**
 *
 * @author Guillermo González de Agüero
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
