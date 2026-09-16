package org.apache.deltaspike.core.impl.scope.window;

import javax.enterprise.context.RequestScoped;

/**
 * Simple class which just provides a &#064;RequestScoped windowId.
 * This assures that there is maximum one single windowId associated
 * with a single Thread or Request. We use &#064;RequestScoped because
 * this also works in async-supported Servlets without having to
 * take care about moving info between ThreadLocals.
 */
@RequestScoped
public class WindowIdHolder
{
    private String windowId;

    /**
     * @return the detected windowId or <code>null</code> if not yet set.
     */
    public String getWindowId()
    {
        return windowId;
    }

    /**
     * Set the windowId for the current thread.
     * @param windowId
     */
    public void setWindowId(String windowId)
    {
        this.windowId = windowId;
    }
}