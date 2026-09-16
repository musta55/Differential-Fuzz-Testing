package org.apache.deltaspike.core.impl.config;

import java.util.Map;

/**
 * Base class for configurations based on regular {@link Map}
 */
public abstract class MapConfigSource extends BaseConfigSource
{
    private final Map<String, String> map;

    public MapConfigSource(Map<String, String> map)
    {
        this.map = map;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return map;
    }

    @Override
    public String getPropertyValue(String key)
    {
        return map.get(key);
    }

    @Override
    public boolean isScannable()
    {
        return true;
    }
}