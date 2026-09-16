package org.apache.deltaspike.servlet.impl.config;

import org.apache.deltaspike.core.impl.config.BaseConfigSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This is an _optional_ ConfigSource!
 * It will only provide information if running in a Servlet container!
 */
public class ServletConfigSource extends BaseConfigSource
{
    private final ConcurrentMap<String, String> servletProperties;

    public ServletConfigSource()
    {
        servletProperties = new ConcurrentHashMap<>();
        initOrdinal(50);
    }

    public void setPropertyValue(String key, String value)
    {
        servletProperties.put(key, value);
    }

    @Override
    public Map<String, String> getProperties()
    {
        return servletProperties;
    }

    @Override
    public String getPropertyValue(String key)
    {
        return servletProperties.get(key);
    }

    @Override
    public String getConfigName()
    {
        return "servletconfig-properties";
    }

    @Override
    public boolean isScannable()
    {
        return true;
    }
}