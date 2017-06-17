package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation;

import es.guillermogonzalezdeaguero.permissionchecking.api.PermissionEvaluator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.DefinitionException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author Guillermo González de Agüero
 */
public class PermissionEvaluatorDetectionExtension<X> implements Extension {

    private static final Logger LOGGER = Logger.getLogger(PermissionEvaluatorDetectionExtension.class.getName());

    private Map<String, Method> methodsMap = new HashMap<>();

    public static class DefaultAnnotationLiteral extends AnnotationLiteral<Default> implements Default {

    }

    public <X> void processAnnotatedType(@Observes @WithAnnotations(PermissionEvaluator.class) ProcessAnnotatedType<X> pat) {
        Set<AnnotatedMethod<? super X>> methods = pat.getAnnotatedType().getMethods();
        for (AnnotatedMethod<? super X> method : methods) {
            if (method.isAnnotationPresent(PermissionEvaluator.class)) {
                String methodName = method.getJavaMember().getName();
                boolean anyMatch = methodsMap.values().stream().map(Method::getName).anyMatch(methodName::equals);
                if (anyMatch) {
                    throw new DefinitionException("Repeated method name for permission evaluator: " + methodName);
                }

                LOGGER.log(Level.INFO, "Detected custom evaluator method: {0}#{1}", new String[]{pat.getAnnotatedType().getJavaClass().toString(), methodName});

                methodsMap.put(methodName, method.getJavaMember());
            }
        }
    }

    public void addBean(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        abd.addBean(new Bean<CustomEvaluatorsBean>() {
            @Override
            public Class<?> getBeanClass() {
                return CustomEvaluatorsBean.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return Collections.emptySet();
            }

            @Override
            public boolean isNullable() {
                return false;
            }

            @Override
            public CustomEvaluatorsBean create(CreationalContext<CustomEvaluatorsBean> creationalContext) {
                return new CustomEvaluatorsBean(methodsMap);
            }

            @Override
            public void destroy(CustomEvaluatorsBean instance, CreationalContext<CustomEvaluatorsBean> creationalContext) {
            }

            @Override
            public Set<Type> getTypes() {
                return new HashSet<>(Arrays.asList(CustomEvaluatorsBean.class, Object.class));
            }

            @Override
            public Set<Annotation> getQualifiers() {
                return Collections.singleton((Annotation) new DefaultAnnotationLiteral());
            }

            @Override
            public Class<? extends Annotation> getScope() {
                return ApplicationScoped.class;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            @Override
            public boolean isAlternative() {
                return false;
            }

        });
        LOGGER.info("Added CustomEvaluatorsBean");
    }
}
