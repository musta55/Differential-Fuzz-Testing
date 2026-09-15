package org.apache.deltaspike.core.api.config.view.metadata;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * {@link ExecutableCallbackDescriptor} for simple callback methods without (supported) parameters, which exposes
 * #execute without parameters.
 *
 * @param <R> return type
 */
public abstract class SimpleCallbackDescriptor<R> extends ExecutableCallbackDescriptor<R>
{
    protected SimpleCallbackDescriptor(Class<?> beanClass, Class<? extends Annotation> callbackMarker)
    {
        super(beanClass, callbackMarker);
    }

    protected SimpleCallbackDescriptor(Class<?>[] beanClasses, Class<? extends Annotation> callbackMarker)
    {
        super(beanClasses, callbackMarker);
    }

    public List<R> execute()
    {
        return super.execute();
    }
}