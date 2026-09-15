package org.apache.deltaspike.core.impl.config;

/**
 * {@link org.apache.deltaspike.core.spi.config.ConfigSource}
 * which uses {@link System#getenv()}
 *
 * We also allow to write underlines _ instead of dots _ in the
 * environment via export (unix) or SET (windows)
 */
class EnvironmentPropertyConfigSource extends MapConfigSource
{
    EnvironmentPropertyConfigSource()
    {
        super(System.getenv());
        initOrdinal(300);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName()
    {
        return "environment-properties";
    }

    @Override
    public String getPropertyValue(String key)
    {
        String val = super.getPropertyValue(key);
        if (val == null || val.isEmpty())
        {
            val = super.getPropertyValue(normalizeKey(key));
        }

        return val;
    }

    private String normalizeKey(String key)
    {
        return key.replace('.', '_');
    }

    @Override
    public boolean isScannable()
    {
        return true;
    }
}