package org.apache.deltaspike.example.scope;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 */
@Named
@SessionScoped
public class SessionScopedBean extends ScopedBean implements Serializable
{
    @PostConstruct
    public void init()
    {
        super.init();
    }
}