package org.apache.deltaspike.core.impl.scope.viewaccess;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.impl.scope.DeltaSpikeContextExtension;
import org.apache.deltaspike.core.spi.scope.viewaccess.ViewAccessContextManager;

import javax.enterprise.inject.Typed;

@Typed()
class InjectableViewAccessContextManager implements ViewAccessContextManager
{
    private transient volatile ViewAccessContextManager viewAccessContextManager;

    public InjectableViewAccessContextManager(ViewAccessContextManager viewAccessContextManager)
    {
        this.viewAccessContextManager = viewAccessContextManager;
    }

    private ViewAccessContextManager getViewAccessContextManager()
    {
        if (this.viewAccessContextManager == null)
        {
            this.viewAccessContextManager =
                BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getViewAccessScopedContext();
        }
        return this.viewAccessContextManager;
    }

    @Override
    public void close()
    {
        getViewAccessContextManager().close();
    }
}