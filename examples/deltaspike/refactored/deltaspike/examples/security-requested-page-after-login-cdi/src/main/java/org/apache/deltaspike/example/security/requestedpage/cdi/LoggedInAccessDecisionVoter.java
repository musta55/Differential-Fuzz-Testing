package org.apache.deltaspike.example.security.requestedpage.cdi;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.security.api.authorization.AbstractAccessDecisionVoter;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Set;

@SessionScoped //or @WindowScoped
public class LoggedInAccessDecisionVoter extends AbstractAccessDecisionVoter
{

    @Inject
    private ViewConfigResolver viewConfigResolver;

    @Inject
    private LoginController loginController;

    private Class<? extends ViewConfig> deniedPage = Pages.Secure.Home.class;

    @Override
    protected void checkPermission(AccessDecisionVoterContext context, Set<SecurityViolation> violations)
    {
        if (!loginController.isLoggedIn())
        {
            violations.add(createSecurityViolation());
            // remember the requested page
            deniedPage = viewConfigResolver
                    .getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId())
                    .getConfigClass();
        }
    }

    private SecurityViolation createSecurityViolation()
    {
        return new SecurityViolation()
        {
            @Override
            public String getReason()
            {
                return "User must be logged in to access this resource";
            }
        };
    }

    public Class<? extends ViewConfig> getDeniedPage()
    {
        return deniedPage;
    }
}