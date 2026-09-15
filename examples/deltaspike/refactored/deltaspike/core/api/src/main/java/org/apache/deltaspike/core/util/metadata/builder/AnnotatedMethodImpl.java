package org.apache.deltaspike.core.util.metadata.builder;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Implementation of {@link AnnotatedMethod} to be used in CDI life cycle events and
 * {@link org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder}.
 */
class AnnotatedMethodImpl<X> extends AnnotatedCallableImpl<X, Method> implements AnnotatedMethod<X>
{
    /**
     * Constructor.
     */
    AnnotatedMethodImpl(AnnotatedType<X> type,
                        Method method,
                        AnnotationStore annotations,
                        Map<Integer, AnnotationStore> parameterAnnotations,
                        Map<Integer, Type> parameterTypeOverrides)
    {
        super(type, method, method.getReturnType(), method.getParameterTypes(), method.getGenericParameterTypes(),
              annotations, parameterAnnotations, method.getGenericReturnType(), parameterTypeOverrides);
    }
}