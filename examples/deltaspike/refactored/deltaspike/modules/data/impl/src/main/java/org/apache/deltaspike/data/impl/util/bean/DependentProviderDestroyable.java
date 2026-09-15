package org.apache.deltaspike.data.impl.util.bean;

import org.apache.deltaspike.core.api.provider.DependentProvider;

public class DependentProviderDestroyable implements Destroyable
{
    private final DependentProvider<?> dependent;

    public DependentProviderDestroyable(DependentProvider<?> dependent)
    {
        this.dependent = dependent;
    }

    @Override
    public void destroy()
    {
        dependent.destroy();
    }
}