package org.apache.deltaspike.core.spi.exception.control.event;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.deltaspike.core.api.exception.control.ExceptionHandlingFlow;

/**
 * Internal view into the ExceptionEvent. Methods on this interface are used by the ExceptionHandlerBroadcaster.
 */
public interface IntrospectiveExceptionEvent<T extends Throwable> extends ExceptionEvent<T>
{
    /**
     * Check to see if this event has been unmuted and therefore called again.
     */
    boolean isUnmute();

    /**
     * The next expected step in the exception handling flow (i.e. abort, rethrow, etc)
     */
    ExceptionHandlingFlow getCurrentExceptionHandlingFlow();

    boolean isBeforeTraversal();

    /**
     * Returns the exception that should be thrown if the next step in the flow is THROW.
     */
    Throwable getThrowNewException();
}