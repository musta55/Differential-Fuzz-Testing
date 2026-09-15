package org.apache.deltaspike.core.impl.message;

import org.apache.deltaspike.core.api.message.MessageInterpolator;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.Locale;

/**
 * {@inheritDoc}
 */
@ApplicationScoped
public class DefaultMessageInterpolator implements MessageInterpolator, Serializable
{
    private static final long serialVersionUID = -8854087197813424812L;

    @Override
    public String interpolate(String messageTemplate, Serializable[] arguments, Locale locale)
    {
        if (messageTemplate == null)
        {
            throw new IllegalArgumentException("messageTemplate cannot be null");
        }
        if (arguments == null || arguments.length == 0)
        {
            return messageTemplate;
        }

        try
        {
            return String.format(locale, messageTemplate, arguments);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error formatting message", e);
        }
    }
}