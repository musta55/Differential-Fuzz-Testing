package org.apache.deltaspike.security.api.authorization;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

import java.util.Set;

public class ErrorViewAwareAccessDeniedException extends AccessDeniedException
{
    private static final long serialVersionUID = 3292231690460417731L;

    private final Class<? extends ViewConfig> errorView;

    public ErrorViewAwareAccessDeniedException(Set<SecurityViolation> violations, Class<? extends ViewConfig> errorView)
    {
        super(violations);
        this.errorView = errorView;
    }

    public Class<? extends ViewConfig> getErrorView()
    {
        return errorView;
    }
}