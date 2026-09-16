package org.apache.deltaspike.core.impl.config;

import org.apache.deltaspike.core.api.config.ConfigResolver;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigHelperImpl implements ConfigResolver.ConfigHelper
{
    @Override
    public Set<String> diffConfig(Map<String, String> oldValues, Map<String, String> newValues)
    {
        oldValues = handleNullMap(oldValues);
        newValues = handleNullMap(newValues);
        Set<String> changedAttribs = new HashSet<>();
        Set<String> oldKeys = new HashSet<>(oldValues.keySet());

        for (Map.Entry<String, String> newPropEntry : newValues.entrySet())
        {
            String key = newPropEntry.getKey();
            if (oldValues.containsKey(key))
            {
                if (compare(oldValues.get(key), newPropEntry.getValue()) != 0)
                {
                    changedAttribs.add(key);
                }
                oldKeys.remove(key);
            }
            else
            {
                changedAttribs.add(key);
            }
        }
        changedAttribs.addAll(oldKeys);

        return changedAttribs;
    }

    private Map<String, String> handleNullMap(Map<String, String> map)
    {
        return map == null ? Collections.emptyMap() : map;
    }

    private int compare(String a, String b)
    {
        if (a == null && b == null)
        {
            return 0;
        }
        if (a != null)
        {
            return a.compareTo(b);
        }
        return 1;
    }
}