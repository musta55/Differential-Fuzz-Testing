package org.apache.deltaspike.example.scope;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 */
@ViewScoped
@Named
public class ViewScopedBean extends ScopedBean implements Serializable
{
    @PostConstruct
    public void init()
    {
        super.init();
    }
}