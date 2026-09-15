package org.apache.deltaspike.jsf.impl.scope.window.strategy;

import org.apache.deltaspike.jsf.impl.util.ClientWindowHelper;

import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Typed;
import org.apache.deltaspike.core.util.StringUtils;

@Dependent
@Typed(LazyWindowStrategy.class)
public class LazyWindowStrategy extends AbstractClientWindowStrategy
{
    @Override
    protected String getOrCreateWindowId(FacesContext facesContext)
    {
        String windowId = ClientWindowHelper.getInitialRedirectWindowId(facesContext);

        if (StringUtils.isEmpty(windowId))
        {
            windowId = getWindowIdParameter(facesContext);
        }

        if (StringUtils.isEmpty(windowId) && isPost(facesContext))
        {
            windowId = getWindowIdPostParameter(facesContext);
        }

        if (StringUtils.isEmpty(windowId))
        {
            handleInitialRedirectOrGenerateNewWindowId(facesContext);
        }

        return windowId;
    }

    private void handleInitialRedirectOrGenerateNewWindowId(FacesContext facesContext)
    {
        if (jsfModuleConfig.isInitialRedirectEnabled() && !isPost(facesContext))
        {
            ClientWindowHelper.handleInitialRedirect(facesContext, generateNewWindowId());
            facesContext.responseComplete();
        }
        else
        {
            generateAndSetNewWindowId();
        }
    }

    private void generateAndSetNewWindowId()
    {
        generateNewWindowId();
    }

    @Override
    protected Map<String, String> createQueryURLParameters(FacesContext facesContext)
    {
        String windowId = getWindowId(facesContext);

        if (windowId == null)
        {
            return null;
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ClientWindowHelper.RequestParameters.GET_WINDOW_ID, windowId);
        return parameters;
    }
    
    @Override
    protected boolean isSupportClientWindowRenderingMode()
    {
        return true;
    }

    @Override
    public boolean isInitialRedirectSupported(FacesContext facesContext)
    {
        return true;
    }
}