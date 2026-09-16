package org.apache.deltaspike.core.impl.config;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Typed;

import org.apache.deltaspike.core.impl.util.JndiUtils;

/**
 * {@link org.apache.deltaspike.core.spi.config.ConfigSource}
 * which uses JNDI for the lookup
 */
@Typed()
class LocalJndiConfigSource extends BaseConfigSource
{
    private static final String BASE_NAME = "java:comp/env/deltaspike/";

    LocalJndiConfigSource()
    {
        initOrdinal(200);
    }

    /**
     * The given key gets used for a lookup via JNDI
     *
     * @param key for the property
     * @return value for the given key or null if there is no configured value
     */
    @Override
    public String getPropertyValue(String key)
    {
        try
        {
            return JndiUtils.lookup(getJndiKey(key), String.class);
        }
        catch (Exception e)
        {
            //do nothing it was just a try
        }
        return null;
    }

    private String getJndiKey(String key)
    {
        return key.startsWith("java:comp/env") ? key : BASE_NAME + key;
    }

    @Override
    public Map<String, String> getProperties()
    {
        Map<String, String> result = new HashMap<>();
        result.putAll(JndiUtils.list(BASE_NAME, String.class));
        result.putAll(JndiUtils.list("java:comp/env", String.class));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName()
    {
        return BASE_NAME;
    }

    @Override
    public boolean isScannable()
    {
        return false;
    }
}