package org.apache.deltaspike.jpa.spi.transaction;

import org.apache.deltaspike.core.spi.InterceptorStrategy;

/**
 * Marker interface for a plugable strategy for {@link org.apache.deltaspike.jpa.api.transaction.Transactional}.
 */
public interface TransactionStrategy extends InterceptorStrategy
{
}