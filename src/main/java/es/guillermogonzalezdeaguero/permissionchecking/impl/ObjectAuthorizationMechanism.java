package es.guillermogonzalezdeaguero.permissionchecking.impl;

import es.guillermogonzalezdeaguero.permissionchecking.api.ObjectPermission;
import es.guillermogonzalezdeaguero.permissionchecking.api.UserObjectPermissionChecker;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.Permission;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

/**
 *
 * @author Guillermo González de Agüero
 */
@ApplicationScoped
public class ObjectAuthorizationMechanism {

    @Inject
    private BeanManager beanManager;

    private Set<Bean<?>> checkerBeans;

    @PostConstruct
    private void init() {
        Set<Bean<?>> beans = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {
        });

        checkerBeans = new HashSet<>();
        for (Bean<?> bean : beans) {
            if (UserObjectPermissionChecker.class.isAssignableFrom(bean.getBeanClass())) {
                checkerBeans.add(bean);
            }
        }
    }

    /**
     * Returning Boolean object in order to be compatible with OmniFaces
     * CDI-JACC current snapshot
     *
     * @param requestedPermission
     * @return
     */
    public Boolean doChecks(Permission requestedPermission) {
        ObjectPermission<?> ourPermission;
        if (!(requestedPermission instanceof ObjectPermission)) {
            return null;
        }

        ourPermission = (ObjectPermission<?>) requestedPermission;

        Class<?> classToCheck = ourPermission.getObject().getClass();

        Bean<?> beanValidador = null;
        for (Bean<?> bean : checkerBeans) {
            for (Type type : bean.getTypes()) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterized = (ParameterizedType) type;

                    if (UserObjectPermissionChecker.class.getName().equals(parameterized.getRawType().getTypeName())) {
                        String parameterClassName = parameterized.getActualTypeArguments()[0].getTypeName();

                        // TODO: CHECK FOR NULL POINTERS!!
                        // TODO: CHECK FOR INHERITANCE
                        if (parameterClassName.equals(classToCheck.getName())) {
                            beanValidador = bean;
                        }
                    }
                }
            }
        }

        if (beanValidador == null) {
            throw new IllegalStateException("No checker available!!");
        }

        UserObjectPermissionChecker<?> reference = (UserObjectPermissionChecker<?>) beanManager.getReference(beanValidador, beanValidador.getBeanClass(), beanManager.createCreationalContext(beanValidador));
        Method[] methods = reference.getClass().getMethods();
        for (Method method : methods) {
            if ("checkPermission".equals(method.getName())
                    && method.getParameterCount() == 1
                    && method.getParameterTypes()[0].isAssignableFrom(ourPermission.getClass())) {
                try {
                    if (!(boolean) method.invoke(reference, ourPermission)) {
                        return false;
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ObjectAuthorizationMechanism.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

}
