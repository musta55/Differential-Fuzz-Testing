package org.apache.deltaspike.jsf.impl.config.view.navigation;

import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameter;
import org.apache.deltaspike.jsf.spi.config.view.navigation.NavigationParameterStrategy;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@NavigationParameter.List({ })
@Interceptor
public class NavigationParameterListInterceptor implements Serializable
{
    private static final long serialVersionUID = 2762625956958428994L;

    @Inject
    private NavigationParameterStrategy navigationParameterStrategy;

    @AroundInvoke
    public Object addParameterList(InvocationContext invocationContext) throws Exception
    {
        return navigationParameterStrategy.execute(invocationContext);
    }
}