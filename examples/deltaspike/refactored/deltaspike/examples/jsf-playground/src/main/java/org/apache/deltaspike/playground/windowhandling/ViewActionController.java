package org.apache.deltaspike.playground.windowhandling;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jsf.spi.scope.window.ClientWindow;

@Named
@RequestScoped
public class ViewActionController
{
    @Inject
    private ClientWindow clientWindow;
    
    private Date lastTimeLinkAction;

    @PostConstruct
    public void init()
    {
        System.out.println("@PostConstruct ViewActionController");
    }

    public void action()
    {
        printWindowId("ViewActionController#action");
    }

    public Date getLastTimeLinkAction()
    {
        return lastTimeLinkAction;
    }

    public void linkAction()
    {
        printWindowId("ViewActionController#linkAction");
        lastTimeLinkAction = new Date();
    }

    private void printWindowId(String methodName)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(methodName + " with windowId: " + clientWindow.getWindowId(context));
    }
}