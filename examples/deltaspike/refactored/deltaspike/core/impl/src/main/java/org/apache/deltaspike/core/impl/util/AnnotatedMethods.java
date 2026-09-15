package org.apache.deltaspike.core.impl.util;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import java.lang.reflect.Method;
import java.util.Optional;

public final class AnnotatedMethods
{
    private AnnotatedMethods()
    {
        // no-op
    }

    public static AnnotatedMethod<?> findMethod(final AnnotatedType<?> type, final Method method)
    {
        return type.getMethods().stream()
                   .filter(am -> am.getJavaMember().equals(method))
                   .findFirst()
                   .orElseThrow(() -> new IllegalStateException("No annotated method for " + method));
    }
}