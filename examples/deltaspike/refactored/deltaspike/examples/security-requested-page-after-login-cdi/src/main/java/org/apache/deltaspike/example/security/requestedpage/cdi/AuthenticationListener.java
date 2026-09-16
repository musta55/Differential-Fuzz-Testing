package org.apache.deltaspike.example.security.requestedpage.cdi;

import org.apache.deltaspike.core.api.config.view.navigation.ViewNavigationHandler;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class AuthenticationListener
{
    @Inject
    private ViewNavigationHandler viewNavigationHandler;

    @Inject
    private LoggedInAccessDecisionVoter loggedInAccessDecisionVoter;

    public void handleLoggedIn(@Observes UserEvent.LoggedIn event) 
    {
        this.viewNavigationHandler.navigateTo(loggedInAccessDecisionVoter.getDeniedPage());
    }

    public void handleFailed(@Observes UserEvent.LoginFailed event)
    {
        this.viewNavigationHandler.navigateTo(Pages.Login.class);
    }

}