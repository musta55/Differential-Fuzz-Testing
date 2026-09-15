package org.apache.deltaspike.example.scope;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 */
@Named
@RequestScoped
public class RequestScopedBean extends ScopedBean
{
    @PostConstruct
    public void init()
    {
        super.init();
    }
}