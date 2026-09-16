package org.apache.deltaspike.core.impl.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.deltaspike.core.spi.config.ConfigSource;

/**
 * Base class for all our ConfigSources
 */
public abstract class BaseConfigSource implements ConfigSource
{
    protected Logger log = Logger.getLogger(getClass().getName());

    private int ordinal = 1000; // default

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrdinal()
    {
        return ordinal;
    }

    /**
     * Init method e.g. for initializing the ordinal.
     * This method can be used from a subclass to determine
     * the ordinal value
     * @param defaultOrdinal the default value for the ordinal if not set via configuration
     */
    protected void initOrdinal(int defaultOrdinal)
    {
        ordinal = defaultOrdinal;
        String configuredOrdinalString = fetchConfiguredOrdinal();
        if (configuredOrdinalString != null)
        {
            parseAndSetOrdinal(configuredOrdinalString);
        }
    }

    private String fetchConfiguredOrdinal()
    {
        return getPropertyValue(ConfigSource.DELTASPIKE_ORDINAL);
    }

    private void parseAndSetOrdinal(String configuredOrdinalString)
    {
        try
        {
            ordinal = Integer.parseInt(configuredOrdinalString.trim());
        }
        catch (NumberFormatException e)
        {
            logInvalidOrdinal(configuredOrdinalString);
        }
    }

    private void logInvalidOrdinal(String configuredOrdinalString)
    {
        log.log(Level.WARNING,
                "The configured config-ordinal isn't a valid integer. Invalid value: " + configuredOrdinalString);
    }
}