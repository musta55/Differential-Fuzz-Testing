package org.apache.deltaspike.core.impl.message;

import org.apache.deltaspike.core.api.message.LocaleResolver;

import javax.enterprise.context.Dependent;
import java.io.Serializable;
import java.util.Locale;

/**
 * Provide the default implementation for picking up the Locale
 * for messages.
 */
@Dependent
public class DefaultLocaleResolver implements LocaleResolver, Serializable
{
    private static final long serialVersionUID = 2075618472090834156L;

    @Override
    public Locale getLocale()
    {
        return Locale.getDefault();
    }
}