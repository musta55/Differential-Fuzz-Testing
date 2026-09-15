package org.apache.deltaspike.cdise.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Adds a listener to the context for enabling requests.
 */
public class CdiServletContextListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        servletContextEvent.getServletContext().addListener(CdiServletRequestListener.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        // Cleanup logic can be added here if necessary
    }
}