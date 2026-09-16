package org.apache.deltaspike.core.impl.scope.viewaccess;

import org.apache.deltaspike.core.impl.scope.DeltaSpikeContextExtension;
import org.apache.deltaspike.core.spi.scope.viewaccess.ViewAccessContextManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ViewAccessContextArtifactProducer
{
    @Inject
    private DeltaSpikeContextExtension deltaSpikeContextExtension;

    @Produces
    @Dependent
    public ViewAccessContextManager getViewAccessContextManager()
    {
        return new InjectableViewAccessContextManager(deltaSpikeContextExtension.getViewAccessScopedContext());
    }
}