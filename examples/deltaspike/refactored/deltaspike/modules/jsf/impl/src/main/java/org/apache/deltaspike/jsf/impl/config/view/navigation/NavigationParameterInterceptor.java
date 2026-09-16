package org.apache.deltaspike.jsf.impl.config.view.navigation;

import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameter;
import org.apache.deltaspike.jsf.spi.config.view.navigation.NavigationParameterStrategy;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@NavigationParameter(key = "", value = "")
@Interceptor
public class NavigationParameterInterceptor implements Serializable
{
    private static final long serialVersionUID = 1762625956958428994L;

    @Inject
    private NavigationParameterStrategy navigationParameterStrategy;

    @AroundInvoke
    public Object addParameter(InvocationContext invocationContext) throws Exception
    {
        return navigationParameterStrategy.execute(invocationContext);
    }
}