package org.apache.deltaspike.core.impl.jmx;

import org.apache.deltaspike.core.api.jmx.JmxBroadcaster;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class JmxBroadcasterLifecycle implements ContextualLifecycle<JmxBroadcaster>
{
    private final DynamicMBeanWrapper delegate;

    public JmxBroadcasterLifecycle(final DynamicMBeanWrapper mbean)
    {
        delegate = mbean;
    }

    @Override
    public JmxBroadcaster create(final Bean<JmxBroadcaster> bean,
                                 final CreationalContext<JmxBroadcaster> creationalContext)
    {
        return delegate;
    }

    @Override
    public void destroy(final Bean<JmxBroadcaster> bean, final JmxBroadcaster instance,
                        final CreationalContext<JmxBroadcaster> creationalContext)
    {
        // no-op
    }
}