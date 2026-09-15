package org.apache.deltaspike.core.impl.jmx;

import java.lang.reflect.Method;

/**
 * Just a helper class mapping a JMX operation.
 */
class Operation
{
    private final Method operation;
    private final boolean presentAsTabularIfPossible;

    Operation(final Method operation, final boolean presentAsTabularIfPossible)
    {
        this.operation = operation;
        this.presentAsTabularIfPossible = presentAsTabularIfPossible;
    }

    boolean isPresentAsTabularIfPossible()
    {
        return presentAsTabularIfPossible;
    }

    Method getOperation()
    {
        return operation;
    }
}