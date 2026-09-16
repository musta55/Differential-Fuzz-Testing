package org.apache.deltaspike.core.impl.config;

/**
 * {@link org.apache.deltaspike.core.spi.config.ConfigSource}
 * which uses {@link System#getProperties()}
 */
class SystemPropertyConfigSource extends PropertiesConfigSource
{
    SystemPropertyConfigSource()
    {
        super(System.getProperties());
        initOrdinal(400);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName()
    {
        return "system-properties";
    }
}