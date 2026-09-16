package org.apache.deltaspike.core.impl.config;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.config.ConfigSnapshot;

import java.util.Map;

public class ConfigSnapshotImpl implements ConfigSnapshot
{
    private final Map<ConfigResolver.TypedResolver<?>, Object> configValues;

    public ConfigSnapshotImpl(Map<ConfigResolver.TypedResolver<?>, Object> configValues)
    {
        this.configValues = configValues;
    }

    public Map<ConfigResolver.TypedResolver<?>, Object> getConfigValues()
    {
        return configValues;
    }
}