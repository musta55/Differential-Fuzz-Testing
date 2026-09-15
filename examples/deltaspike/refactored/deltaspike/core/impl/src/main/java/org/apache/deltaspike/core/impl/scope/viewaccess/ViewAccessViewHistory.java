package org.apache.deltaspike.core.impl.scope.viewaccess;

import java.io.Serializable;
import org.apache.deltaspike.core.api.scope.WindowScoped;

@WindowScoped
public class ViewAccessViewHistory implements Serializable
{
    private static final long serialVersionUID = 8917607910721148527L;
    
    private String lastView;

    public String getLastView()
    {
        return lastView;
    }

    public void setLastView(String lastView)
    {
        this.lastView = lastView;
    }
}