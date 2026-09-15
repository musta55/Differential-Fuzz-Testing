package org.apache.deltaspike.security.impl.authorization;

import org.apache.deltaspike.security.api.authorization.AccessDeniedException;

//just to avoid a 2nd call of the handlers
//the first one can't be removed, because we need an active AccessDecisionVoterContext
public class SkipInternalProcessingException extends RuntimeException
{
    private static final long serialVersionUID = 3585306529694592791L;

    private final AccessDeniedException accessDeniedException;

    public SkipInternalProcessingException(AccessDeniedException accessDeniedException)
    {
        this.accessDeniedException = accessDeniedException;
    }

    public AccessDeniedException getAccessDeniedException()
    {
        return accessDeniedException;
    }
}