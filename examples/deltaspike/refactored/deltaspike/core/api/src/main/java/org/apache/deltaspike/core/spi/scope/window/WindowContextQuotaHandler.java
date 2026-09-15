package org.apache.deltaspike.core.spi.scope.window;

import org.apache.deltaspike.core.spi.activation.Deactivatable;

import java.io.Serializable;

/**
 * Allows to create a custom handler for a custom window-quota
 */
public interface WindowContextQuotaHandler extends Deactivatable, Serializable
{
    void checkWindowContextQuota(String windowId);
}