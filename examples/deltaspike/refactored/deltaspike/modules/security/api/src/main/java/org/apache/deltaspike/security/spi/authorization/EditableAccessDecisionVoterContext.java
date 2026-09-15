package org.apache.deltaspike.security.spi.authorization;

import org.apache.deltaspike.security.api.authorization.AccessDecisionState;
import org.apache.deltaspike.security.api.authorization.AccessDecisionVoterContext;
import org.apache.deltaspike.security.api.authorization.SecurityViolation;

/**
 * Interface which allows to provide a custom {@link AccessDecisionVoterContext} implementation
 */
public interface EditableAccessDecisionVoterContext extends AccessDecisionVoterContext
{
    /**
     * Allows to add custom meta-data. The default security strategy adds custom annotations of the intercepted method
     * as well as class-level annotations. (Currently inherited annotations aren't supported)
     * @param key key for the meta-data
     * @param metaData meta-data which should be added
     */
    void addMetaData(String key, Object metaData);

    /**
     * Updates the state of the context
     * @param accessDecisionVoterState current state
     */
    void setState(AccessDecisionState accessDecisionVoterState);

    /**
     * Sets the source of the context, typically the invocation context.
     * @param source e.g. the invocation-context
     */
    void setSource(Object source);

    /**
     * Adds a new {@link SecurityViolation} to the context
     * @param securityViolation security-violation which should be added
     */
    void addViolation(SecurityViolation securityViolation);
}