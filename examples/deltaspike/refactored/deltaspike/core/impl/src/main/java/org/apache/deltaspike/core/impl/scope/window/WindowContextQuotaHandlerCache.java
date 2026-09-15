package org.apache.deltaspike.core.impl.scope.window;

import org.apache.deltaspike.core.spi.scope.window.WindowContext;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;

@RequestScoped
public class WindowContextQuotaHandlerCache implements Serializable
{
    private String checkedWindowId;
    private String windowIdToRemove;

    @Inject
    private WindowContext windowContext;

    /**
     * @param currentWindowId window-id which gets processed right now
     * @return true if the previously checked window-id is the same, false otherwise
     */
    public boolean cacheWindowId(String currentWindowId)
    {
        boolean isSameAsChecked = currentWindowId.equals(checkedWindowId);
        checkedWindowId = currentWindowId;
        return isSameAsChecked;
    }

    public void setWindowIdToDestroy(String windowIdToRemove)
    {
        this.windowIdToRemove = windowIdToRemove;
    }

    @PreDestroy
    public void cleanup()
    {
        if (windowIdToRemove != null)
        {
            windowContext.closeWindow(windowIdToRemove);
        }
    }
}