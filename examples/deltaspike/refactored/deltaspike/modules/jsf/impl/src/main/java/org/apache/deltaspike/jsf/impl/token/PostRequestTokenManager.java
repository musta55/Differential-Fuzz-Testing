package org.apache.deltaspike.jsf.impl.token;

import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.jsf.api.config.JsfModuleConfig;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.UUID;

@WindowScoped
@Named("dsPostRequestToken")
public class PostRequestTokenManager implements Serializable
{
    private static final long serialVersionUID = 5387627547198129897L;

    private volatile String currentToken;

    private boolean allowPostRequestWithoutDoubleSubmitPrevention = true;

    protected PostRequestTokenManager()
    {
    }

    @Inject
    public PostRequestTokenManager(JsfModuleConfig config)
    {
        this.allowPostRequestWithoutDoubleSubmitPrevention = config.isAllowPostRequestWithoutDoubleSubmitPrevention();
    }

    public void createNewToken()
    {
        this.currentToken = UUID.randomUUID().toString().replace("-", "");
    }

    public synchronized boolean isValidRequest(String token)
    {
        if (token == null)
        {
            return this.allowPostRequestWithoutDoubleSubmitPrevention;
        }
        String previousToken = this.currentToken;
        createNewToken();

        return isTokenValid(token, previousToken);
    }

    private boolean isTokenValid(String token, String previousToken)
    {
        return token.equals(previousToken);
    }

    public String getCurrentToken()
    {
        return this.currentToken;
    }
}