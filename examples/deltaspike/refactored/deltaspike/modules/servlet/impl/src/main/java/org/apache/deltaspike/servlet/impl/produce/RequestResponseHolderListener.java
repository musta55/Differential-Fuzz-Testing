package org.apache.deltaspike.servlet.impl.produce;

import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * This class stores the ServletRequest in the {@link RequestResponseHolder}.
 */
public class RequestResponseHolderListener implements ServletRequestListener, Deactivatable
{

    private final boolean activated;

    public RequestResponseHolderListener()
    {
        this.activated = ClassDeactivationUtils.isActivated(this.getClass());
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre)
    {
        if (isActivated())
        {
            /*
             * For some reason Tomcat seems to call requestInitialized() more than
             * once for a request. Not sure if this allowed according to the spec.
             */
            if (!RequestResponseHolder.REQUEST.isBound())
            {
                RequestResponseHolder.REQUEST.bind(sre.getServletRequest());
            }
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre)
    {
        if (isActivated())
        {
            RequestResponseHolder.REQUEST.release();
        }
    }

    private boolean isActivated()
    {
        return activated;
    }
}