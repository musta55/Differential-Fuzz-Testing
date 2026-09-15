package org.apache.deltaspike.jsf.impl.scope.window.strategy;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;

@Dependent
@Typed(NoneWindowStrategy.class)
public class NoneWindowStrategy extends AbstractClientWindowStrategy
{
    @Override
    protected String getOrCreateWindowId(FacesContext facesContext)
    {
        return DEFAULT_WINDOW_ID;
    }
}