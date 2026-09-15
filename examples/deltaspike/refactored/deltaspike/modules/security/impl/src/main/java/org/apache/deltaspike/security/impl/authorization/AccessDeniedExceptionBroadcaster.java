package org.apache.deltaspike.security.impl.authorization;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.security.api.authorization.AccessDeniedException;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

//this broadcaster just allows to change the default behavior (if needed)
//needed because it needs to be possible to 'consume' exceptions of type AccessDeniedException.
//instead of ignoring the result of exception-control and throwing them in any case (like we have to do it per default).
@Dependent
public class AccessDeniedExceptionBroadcaster
{
    @Inject
    private BeanManager beanManager;

    public void broadcastAccessDeniedException(AccessDeniedException accessDeniedException)
    {
        ExceptionToCatchEvent exceptionToCatchEvent = new ExceptionToCatchEvent(accessDeniedException);

        try
        {
            this.beanManager.fireEvent(exceptionToCatchEvent);
        }
        catch (AccessDeniedException e)
        {
            throw new SkipInternalProcessingException(accessDeniedException);
        }
        //we have to throw it in any case to support "observers" for AccessDeniedException (see DELTASPIKE-636)
        //however, currently we can't do it based on the exception-control api (see DELTASPIKE-638)
        throw new SkipInternalProcessingException(accessDeniedException);
    }
}