package org.apache.deltaspike.partialbean.impl;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ReflectionUtils;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import java.lang.reflect.Method;

public class DeltaSpikePartialProducerLifecycle<T> implements ContextualLifecycle<T>
{
    private final Class<?> targetPartialBeanClass;
    private final Method producerMethod;

    public DeltaSpikePartialProducerLifecycle(Class<?> targetPartialBeanClass, Method producerMethod)
    {
        this.targetPartialBeanClass = targetPartialBeanClass;
        this.producerMethod = producerMethod;
    }

    @Override
    public T create(Bean<T> bean, CreationalContext<T> creationalContext)
    {
        Object partialBean = BeanProvider.getContextualReference(targetPartialBeanClass);
        return (T) ReflectionUtils.invokeMethod(partialBean, producerMethod, Object.class, false);
    }

    @Override
    public void destroy(Bean<T> bean, T instance, CreationalContext<T> creationalContext)
    {
        //we don't need to support disposer-methods for now (because it's just about exposing injectable values)
    }
}