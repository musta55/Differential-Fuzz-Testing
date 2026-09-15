package org.apache.deltaspike.servlet.impl.event;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.deltaspike.core.api.literal.DestroyedLiteral;
import org.apache.deltaspike.core.api.literal.InitializedLiteral;

/**
 * This class listens for servlet context events and forwards them to the CDI event bus.
 */
public class EventBridgeContextListener extends EventBroadcaster implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        if (isActivated())
        {
            fireEvent(sce.getServletContext(), InitializedLiteral.INSTANCE);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        if (isActivated())
        {
            fireEvent(sce.getServletContext(), DestroyedLiteral.INSTANCE);
        }
    }
}