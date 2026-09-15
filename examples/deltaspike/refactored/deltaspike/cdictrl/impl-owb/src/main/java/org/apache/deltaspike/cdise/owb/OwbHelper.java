package org.apache.deltaspike.cdise.owb;

import javax.servlet.ServletContextEvent;

/**
 * A few utility methods for OWB
 */
public class OwbHelper
{
    private OwbHelper()
    {
        // just to prevent initialisation
    }

    public static Object getMockSession(String sessionId)
    {
        return new MockHttpSession(sessionId);
    }

    public static Object getMockServletContextEvent()
    {
        return new ServletContextEvent(MockServletContext.getInstance());
    }

    public static Object getMockServletContext()
    {
        return MockServletContext.getInstance();
    }
}