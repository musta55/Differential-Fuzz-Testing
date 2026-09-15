package org.apache.deltaspike.core.impl.exception.control;

import org.apache.deltaspike.core.impl.exception.control.extension.ExceptionControlExtension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class HandlerMethodStorageProducer
{
    @Inject
    private ExceptionControlExtension exceptionControlExtension;

    @Produces
    @ApplicationScoped
    protected HandlerMethodStorage createHandlerMethodStorage()
    {
        return new HandlerMethodStorageImpl(exceptionControlExtension.getAllExceptionHandlers());
    }
}