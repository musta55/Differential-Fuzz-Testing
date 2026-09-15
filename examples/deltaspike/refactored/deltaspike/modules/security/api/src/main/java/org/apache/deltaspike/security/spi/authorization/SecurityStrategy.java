package org.apache.deltaspike.security.spi.authorization;

import org.apache.deltaspike.core.spi.InterceptorStrategy;

/**
 * Marker interface for a pluggable strategy for
 * {@link org.apache.deltaspike.security.api.authorization.Secured}
 */
public interface SecurityStrategy extends InterceptorStrategy
{
}