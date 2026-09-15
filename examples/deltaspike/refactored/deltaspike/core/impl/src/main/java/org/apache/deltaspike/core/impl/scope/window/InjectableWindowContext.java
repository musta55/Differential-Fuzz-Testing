package org.apache.deltaspike.core.impl.scope.window;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.impl.scope.DeltaSpikeContextExtension;
import org.apache.deltaspike.core.spi.scope.window.WindowContext;

import javax.enterprise.inject.Typed;

//keep it public for supporting #{dsWindowContext.getCurrentWindowId()} in addition to
//#{dsWindowContext.currentWindowId}
@Typed()
public class InjectableWindowContext implements WindowContext
{
    private static final long serialVersionUID = -3606786361833889628L;

    private transient volatile WindowContext windowContext;

    InjectableWindowContext(WindowContext windowContext)
    {
        this.windowContext = windowContext;
    }

    private WindowContext getWindowContext()
    {
        if (windowContext == null)
        {
            windowContext = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getWindowContext();
        }
        return windowContext;
    }

    @Override
    public String getCurrentWindowId()
    {
        return getWindowContext().getCurrentWindowId();
    }

    @Override
    public void activateWindow(String windowId)
    {
        getWindowContext().activateWindow(windowId);
    }

    @Override
    public boolean closeWindow(String windowId)
    {
        return getWindowContext().closeWindow(windowId);
    }
}