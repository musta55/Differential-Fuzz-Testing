package org.apache.deltaspike.core.impl.config;

import org.apache.deltaspike.core.api.config.Config;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.util.ClassUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class ConfigProviderImpl implements ConfigResolver.ConfigProvider
{
    /**
     * The content of this map will get lazily initiated and will hold the
     * Configs for each WebApp/EAR, etc (thus the ClassLoader).
     */
    private static Map<ClassLoader, ConfigImpl> configs = new ConcurrentHashMap<>();

    @Override
    public Config getConfig()
    {
        ClassLoader cl = ClassUtils.getClassLoader(null);
        return getConfig(cl);
    }

    @Override
    public Config getConfig(ClassLoader cl)
    {
        ConfigImpl config = configs.get(cl);
        if (config == null)
        {
            config = new ConfigImpl(cl);
            config.init();
            ConfigImpl oldConfig = configs.put(cl, config);
            if (oldConfig != null)
            {
                config = oldConfig;
            }
        }
        return config;
    }

    @Override
    public void releaseConfig(ClassLoader cl)
    {
        ConfigImpl oldConfig = configs.remove(cl);
        if (oldConfig != null)
        {
            oldConfig.release();
        }

        // And remove all the children as well.
        // This will e.g happen in EAR scenarios
        removeChildConfigs(cl);
    }

    @Override
    public ConfigResolver.ConfigHelper getHelper()
    {
        return new ConfigHelperImpl();
    }

    private boolean isChildClassLoader(ClassLoader configClassLoader, ClassLoader suspect)
    {
        ClassLoader suspectParentCl = suspect.getParent();
        if (suspectParentCl == null)
        {
            return false;
        }

        if (suspectParentCl == configClassLoader)
        {
            return true;
        }

        return isChildClassLoader(configClassLoader, suspectParentCl);
    }

    private void removeChildConfigs(ClassLoader cl)
    {
        Iterator<Map.Entry<ClassLoader, ConfigImpl>> it = configs.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<ClassLoader, ConfigImpl> cfgEntry = it.next();
            if (isChildClassLoader(cl, cfgEntry.getKey()))
            {
                cfgEntry.getValue().release();
                it.remove();
            }
        }
    }
}