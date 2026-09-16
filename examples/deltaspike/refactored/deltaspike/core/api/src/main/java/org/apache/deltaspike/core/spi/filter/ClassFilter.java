package org.apache.deltaspike.core.spi.filter;

import org.apache.deltaspike.core.spi.activation.Deactivatable;

public interface ClassFilter extends Deactivatable
{
    boolean isFiltered(Class<?> currentClass);
}