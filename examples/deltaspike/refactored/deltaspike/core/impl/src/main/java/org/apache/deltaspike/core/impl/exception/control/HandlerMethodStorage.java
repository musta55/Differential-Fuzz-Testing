package org.apache.deltaspike.core.impl.exception.control;

import org.apache.deltaspike.core.api.exception.control.HandlerMethod;

import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

/**
 * Injectable storage to support programmatic registration and lookup of
 * {@link org.apache.deltaspike.core.api.exception.control.HandlerMethod} instances.
 */
//X TODO move it to the spi package - otherwise there is no need for an interface
public interface HandlerMethodStorage
{
    /**
     * Registers the given handlerMethod to the storage.
     *
     * @param handlerMethod HandlerMethod implementation to register with the storage
     */
    <T extends Throwable> void registerHandlerMethod(HandlerMethod<T> handlerMethod);

    /**
     * Obtains the applicable handlers for the given type or super type of the given type to order the handlers.
     *
     * @param exceptionClass    Type of exception to narrow handler list
     * @param bm                active BeanManager
     * @param handlerQualifiers additional handlerQualifiers to limit handlers
     * @param isBefore          traversal limiter
     * @return An order collection of handlers for the given type.
     */
    Collection<HandlerMethod<? extends Throwable>> getHandlersForException(Type exceptionClass, BeanManager bm,
                                                                           Set<Annotation> handlerQualifiers,
                                                                           boolean isBefore);
}