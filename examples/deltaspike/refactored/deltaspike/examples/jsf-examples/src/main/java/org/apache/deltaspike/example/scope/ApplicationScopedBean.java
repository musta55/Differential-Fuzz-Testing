package org.apache.deltaspike.example.scope;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 */
@Named
@ApplicationScoped
public class ApplicationScopedBean extends ScopedBean
{
    @PostConstruct
    public void init()
    {
        super.init();
    }
}