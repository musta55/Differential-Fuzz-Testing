package org.apache.deltaspike.jsf.impl.scope.window;

import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;
import java.util.Collections;
import java.util.Map;

//ATTENTION: don't rename/move this class as long as we need the workaround in impl-ee6
//(further details are available at: DELTASPIKE-655 and DELTASPIKE-659)

/**
 * This adapter supports two use-cases:
 * #1: Using the window-handling of DeltaSpike also for JSF internals like state-handling
 * #2: Using the window-handling of JSF for DeltaSpike (if the corresponding JSF-config is available)
 */
public class ClientWindowAdapter extends ClientWindow
{
    private final org.apache.deltaspike.jsf.spi.scope.window.ClientWindow window;

    public ClientWindowAdapter(org.apache.deltaspike.jsf.spi.scope.window.ClientWindow window)
    {
        this.window = window;
    }

    @Override
    public void decode(FacesContext context)
    {
        //currently not needed by the window-handling of DeltaSpike
    }

    @Override
    public String getId()
    {
        return window.getWindowId(FacesContext.getCurrentInstance());
    }

    @Override
    public Map<String, String> getQueryURLParameters(FacesContext context)
    {
        //currently not needed by the window-handling of DeltaSpike
        return Collections.emptyMap();
    }

    @Override
    public boolean isClientWindowRenderModeEnabled(FacesContext context)
    {
        return false;
    }
}