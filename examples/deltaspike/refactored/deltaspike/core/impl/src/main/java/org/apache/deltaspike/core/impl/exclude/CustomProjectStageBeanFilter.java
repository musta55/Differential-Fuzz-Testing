package org.apache.deltaspike.core.impl.exclude;

import org.apache.deltaspike.core.spi.activation.Deactivatable;

/**
 * Allows to deactivate the autom. filtering of custom project-stages -> @Typed() needs to be used manually
 */
public interface CustomProjectStageBeanFilter extends Deactivatable
{
}