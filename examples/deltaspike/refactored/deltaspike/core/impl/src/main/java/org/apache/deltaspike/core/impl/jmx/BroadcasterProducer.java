package org.apache.deltaspike.core.impl.jmx;

import org.apache.deltaspike.core.api.jmx.JmxBroadcaster;
import org.apache.deltaspike.core.api.jmx.MBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@ApplicationScoped
public class BroadcasterProducer
{
    @Inject
    private MBeanExtension extension;

    @Produces
    @Dependent
    public JmxBroadcaster jmxBroadcaster(final InjectionPoint ip)
    {
        final Class<?> declaringClass = ip.getMember().getDeclaringClass();
        final JmxBroadcaster broadcaster = extension.getBroadcasterFor(declaringClass);
        if (broadcaster == null)
        {
            //TODO discuss validation during bootstrapping
            throw new IllegalStateException(createExceptionMessage(declaringClass));
        }
        return broadcaster;
    }

    private String createExceptionMessage(Class<?> declaringClass) {
        return "Invalid injection of " + JmxBroadcaster.class.getName() +
                " in " + declaringClass.getName() + " detected. It is required to annotate the class with @" +
                MBean.class.getName();
    }
}