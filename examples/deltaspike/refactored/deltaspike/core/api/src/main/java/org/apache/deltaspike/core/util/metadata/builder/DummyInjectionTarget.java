package org.apache.deltaspike.core.util.metadata.builder;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Injection target implementation that does nothing
 */
public class DummyInjectionTarget<T> implements InjectionTarget<T>
{
    @Override
    public void inject(T instance, CreationalContext<T> ctx)
    {
    }

    @Override
    public void postConstruct(T instance)
    {
    }

    @Override
    public void preDestroy(T instance)
    {
    }

    @Override
    public void dispose(T instance)
    {
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints()
    {
        return emptySet();
    }

    @Override
    public T produce(CreationalContext<T> ctx)
    {
        return null;
    }
}