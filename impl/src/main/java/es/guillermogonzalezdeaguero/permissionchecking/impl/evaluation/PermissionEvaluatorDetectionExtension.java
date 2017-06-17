package es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation;

import es.guillermogonzalezdeaguero.permissionchecking.api.PermissionEvaluator;
import es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine.Engine;
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
import es.guillermogonzalezdeaguero.permissionchecking.api.Allowed;
import es.guillermogonzalezdeaguero.permissionchecking.impl.evaluation.engine.InvalidExpressionException;

/**
 *
 * @author Guillermo González de Agüero
 */
public class PermissionEvaluatorDetectionExtension<X> implements Extension {

    private static final Logger LOGGER = Logger.getLogger(PermissionEvaluatorDetectionExtension.class.getName());

    private Map<String, Method> methodsMap = new HashMap<>();

    private Set<String> expressionValues = new HashSet<>();

    public static class DefaultAnnotationLiteral extends AnnotationLiteral<Default> implements Default {

    }

    public <X> void processAnnotatedType(@Observes @WithAnnotations(PermissionEvaluator.class) ProcessAnnotatedType<X> pat) {
        Set<AnnotatedMethod<? super X>> methods = pat.getAnnotatedType().getMethods();
        for (AnnotatedMethod<? super X> annotatedMethod : methods) {
            if (annotatedMethod.isAnnotationPresent(PermissionEvaluator.class)) {
                Method method = annotatedMethod.getJavaMember();
                String methodName = method.getName();

                if (!Boolean.class.isAssignableFrom(method.getReturnType()) && method.getReturnType() != boolean.class) {
                    throw new DefinitionException("Methods for permission evaluation must return boolean: " + pat.getAnnotatedType().getJavaClass() + "#" + methodName);
                }

                boolean anyMatch = methodsMap.values().stream().map(Method::getName).anyMatch(methodName::equals);
                if (anyMatch) {
                    throw new DefinitionException("Repeated method name for permission evaluator: " + methodName);
                }

                LOGGER.log(Level.INFO, "Detected custom evaluator method: {0}#{1}", new String[]{pat.getAnnotatedType().getJavaClass().toString(), methodName});

                methodsMap.put(methodName, method);
            }
        }
    }

    public <X> void processAllowed(@Observes @WithAnnotations(Allowed.class) ProcessAnnotatedType<X> pat) {
        pat.getAnnotatedType().
                getMethods().
                stream().
                filter(am -> am.isAnnotationPresent(Allowed.class)).
                map(am -> am.getAnnotation(Allowed.class)).
                map(a -> a.value()).
                peek(s -> LOGGER.log(Level.INFO, "Saving expression: {0}", s)).
                forEach(expressionValues::add);
    }

    public void validateExpressions(@Observes AfterBeanDiscovery abd, BeanManager bm) {

        Engine engine = new Engine(methodsMap.values());

        try {
            for (String expressionValue : expressionValues) {
                engine.parseExpression(expressionValue);
            }

            abd.addBean(new Bean<Engine>() {
                @Override
                public Class<?> getBeanClass() {
                    return Engine.class;
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
                public Engine create(CreationalContext<Engine> creationalContext) {
                    return engine;
                }

                @Override
                public void destroy(Engine instance, CreationalContext<Engine> creationalContext) {
                }

                @Override
                public Set<Type> getTypes() {
                    return new HashSet<>(Arrays.asList(Engine.class, Object.class));
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
            LOGGER.info("Added CustomEvaluator Engine");
        } catch (InvalidExpressionException e) {
            abd.addDefinitionError(e);
        }
    }
}
