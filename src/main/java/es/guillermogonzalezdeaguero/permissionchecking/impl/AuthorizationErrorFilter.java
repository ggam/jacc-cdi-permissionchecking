package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.AuthorizationException;
import java.io.IOException;
import javax.ejb.EJBException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Guillermo González de Agüero
 */
@WebFilter("/*")
public class AuthorizationErrorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (AuthorizationException e) {
            sendError(e, response);
        } catch (EJBException e) {
            // Check for EJBExceptions hiding AuthorizationExceptions
            Throwable cause = e.getCause();
            if (cause instanceof AuthorizationException) {
                sendError((AuthorizationException) cause, response);
            } else {
                // Do nothing and propagate the exception
                throw e;
            }
        }
    }

    private void sendError(AuthorizationException exception, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
    }

    @Override
    public void destroy() {

    }

}
