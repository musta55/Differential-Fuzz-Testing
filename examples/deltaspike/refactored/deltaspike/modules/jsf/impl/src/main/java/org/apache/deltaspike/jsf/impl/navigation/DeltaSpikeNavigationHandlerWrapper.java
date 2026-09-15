package org.apache.deltaspike.jsf.impl.navigation;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.ConfigurableNavigationHandlerWrapper;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.Set;

//ATTENTION: don't rename/move this class as long as we need the workaround in impl-ee6
//(further details are available at: DELTASPIKE-655 and DELTASPIKE-659)

@SuppressWarnings("UnusedDeclaration")
public class DeltaSpikeNavigationHandlerWrapper extends ConfigurableNavigationHandlerWrapper
{
    private final ConfigurableNavigationHandler wrapped;
    private final DeltaSpikeNavigationHandler deltaSpikeNavigationHandler;

    public DeltaSpikeNavigationHandlerWrapper(ConfigurableNavigationHandler wrapped)
    {
        this.wrapped = wrapped;
        //only for delegating the methods implemented by DeltaSpikeNavigationHandler
        this.deltaSpikeNavigationHandler = new DeltaSpikeNavigationHandler(wrapped);
    }

    @Override
    public void handleNavigation(FacesContext context, String fromAction, String outcome)
    {
        deltaSpikeNavigationHandler.handleNavigation(context, fromAction, outcome);
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases()
    {
        return deltaSpikeNavigationHandler.getNavigationCases();
    }

    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome)
    {
        return deltaSpikeNavigationHandler.getNavigationCase(context, fromAction, outcome);
    }

    public ConfigurableNavigationHandler getWrapped()
    {
        return wrapped;
    }
}