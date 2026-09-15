package org.apache.deltaspike.core.impl.exception.control;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.util.metadata.builder.ParameterValueRedefiner;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

/**
 * Redefiner allowing to inject a non contextual instance of {@link DefaultExceptionEvent} into the first parameter.
 * This class is immutable.
 */
class OutboundParameterValueRedefiner implements ParameterValueRedefiner
{
    private final ExceptionEvent<?> event;
    private final Bean<?> declaringBean;
    private final HandlerMethodImpl<?> handlerMethod;

    /**
     * Sole constructor.
     *
     * @param event         instance of DefaultExceptionEvent to inject.
     * @param handlerMethod Handler method this redefiner is for
     */
    OutboundParameterValueRedefiner(final ExceptionEvent<?> event, final HandlerMethodImpl<?> handlerMethod)
    {
        this.event = event;
        declaringBean = handlerMethod.getDeclaringBean();
        this.handlerMethod = handlerMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object redefineParameterValue(ParameterValue value)
    {
        CreationalContext<?> ctx = createCreationalContext();

        try
        {
            if (value.getPosition() == handlerMethod.getHandlerParameter().getPosition())
            {
                return event;
            }
            return value.getDefaultValue(ctx);
        }
        finally
        {
            releaseCreationalContext(ctx);
        }
    }

    private CreationalContext<?> createCreationalContext()
    {
        return BeanManagerProvider.getInstance().getBeanManager().createCreationalContext(declaringBean);
    }

    private void releaseCreationalContext(CreationalContext<?> ctx)
    {
        if (ctx != null)
        {
            ctx.release();
        }
    }
}