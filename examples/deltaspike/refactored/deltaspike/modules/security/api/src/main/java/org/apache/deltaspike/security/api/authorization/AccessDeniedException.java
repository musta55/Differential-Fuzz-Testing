package org.apache.deltaspike.security.api.authorization;

import java.util.Set;

/**
 * Exception occurs in case of a security-violation.
 * It's aware of the reason for the violation as well as the error-view which should be used to display the restriction.
 */
public class AccessDeniedException extends SecurityException
{
    private static final long serialVersionUID = -4066763895951237969L;

    private final Set<SecurityViolation> violations;

    /**
     * Constructor for creating the exception for the given violations and error-view
     * @param violations current violations
     */
    public AccessDeniedException(Set<SecurityViolation> violations)
    {
        this.violations = violations;
    }

    /**
     * All {@link SecurityViolation} which were found by a {@link AccessDecisionVoter}
     *
     * @return all security-violations
     */
    public Set<SecurityViolation> getViolations()
    {
        return violations;
    }
}