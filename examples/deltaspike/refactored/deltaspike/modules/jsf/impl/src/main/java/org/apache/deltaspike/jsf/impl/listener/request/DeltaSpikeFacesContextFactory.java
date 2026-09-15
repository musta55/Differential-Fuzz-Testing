package org.apache.deltaspike.jsf.impl.listener.request;

import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.jsf.spi.scope.window.ClientWindow;

public class DeltaSpikeFacesContextFactory extends FacesContextFactory implements Deactivatable
{
    private final FacesContextFactory wrappedFacesContextFactory;

    private final boolean deactivated;
    
    private volatile Boolean initialized;
    
    private ClientWindow clientWindow;

    /**
     * Constructor for wrapping the given {@link FacesContextFactory}
     *
     * @param wrappedFacesContextFactory wrapped faces-context-factory which should be used
     */
    public DeltaSpikeFacesContextFactory(FacesContextFactory wrappedFacesContextFactory)
    {
        this.wrappedFacesContextFactory = wrappedFacesContextFactory;
        this.deactivated = !ClassDeactivationUtils.isActivated(getClass());
    }

    /**
     * Wrapps the created {@link javax.faces.context.FacesContext} with {@link DeltaSpikeFacesContextWrapper}
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public FacesContext getFacesContext(Object context,
                                        Object request,
                                        Object response,
                                        Lifecycle lifecycle)
    {
        FacesContext facesContext =
                this.wrappedFacesContextFactory.getFacesContext(context, request, response, lifecycle);

        if (facesContext == null || this.deactivated || facesContext instanceof DeltaSpikeFacesContextWrapper)
        {
            return facesContext;
        }

        lazyInit();
        
        return new DeltaSpikeFacesContextWrapper(facesContext, clientWindow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FacesContextFactory getWrapped()
    {
        return wrappedFacesContextFactory;
    }
    
    private void lazyInit()
    {
        if (initialized == null)
        {
            init();
        }
    }

    private synchronized void init()
    {
        if (initialized == null)
        {
            clientWindow = BeanProvider.getContextualReference(ClientWindow.class, true);
            initialized = true;
        }
    }
}