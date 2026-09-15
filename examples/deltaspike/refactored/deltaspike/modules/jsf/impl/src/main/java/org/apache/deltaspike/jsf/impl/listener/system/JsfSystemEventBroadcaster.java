package org.apache.deltaspike.jsf.impl.listener.system;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;

/**
 * Broadcasts JSF events to CDI observers.
 */
public class JsfSystemEventBroadcaster implements SystemEventListener, Deactivatable
{
    private final boolean isActivated;

    public JsfSystemEventBroadcaster()
    {
        this.isActivated = ClassDeactivationUtils.isActivated(getClass());
    }

    @Override
    public boolean isListenerForSource(Object source)
    {
        return true;
    }

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException
    {
        if (!isActivated)
        {
            return;
        }

        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        beanManager.fireEvent(event);
    }
}