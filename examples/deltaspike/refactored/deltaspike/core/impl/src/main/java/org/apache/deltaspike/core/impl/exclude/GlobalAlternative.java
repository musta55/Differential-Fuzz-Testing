package org.apache.deltaspike.core.impl.exclude;

import org.apache.deltaspike.core.spi.activation.Deactivatable;

/**
 * Allows to deactivate only the global alternative feature (needed if we keep it in the exclude extension)
 */
public interface GlobalAlternative extends Deactivatable
{
}