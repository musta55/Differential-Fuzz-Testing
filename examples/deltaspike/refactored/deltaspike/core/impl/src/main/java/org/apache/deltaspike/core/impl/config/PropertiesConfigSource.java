package org.apache.deltaspike.core.impl.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for configuration sources based on a fixed {@link Properties} object.
 */
public abstract class PropertiesConfigSource extends BaseConfigSource
{
    private final Properties properties;

    protected PropertiesConfigSource(Properties properties)
    {
        this.properties = properties;
    }

    /**
     * The given key gets used for a lookup via a properties object
     *
     * @param key for the property
     * @return value for the given key or null if there is no configured value
     */
    @Override
    public String getPropertyValue(String key)
    {
        return properties.getProperty(key);
    }

    @Override
    public Map<String, String> getProperties()
    {
        Map<String, String> result = new HashMap<>(properties.size());
        for (String propertyName : properties.stringPropertyNames())
        {
            result.put(propertyName, properties.getProperty(propertyName));
        }
        return result;
    }

    @Override
    public boolean isScannable()
    {
        return true;
    }
}