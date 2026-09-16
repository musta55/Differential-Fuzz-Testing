package org.apache.deltaspike.example.config;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

import javax.enterprise.context.ApplicationScoped;

/**
 * Allows to use a different file than apache-deltaspike.properties
 */
@ApplicationScoped
public class CustomPropertyFileConfig implements PropertyFileConfig
{
    @Override
    public String getPropertyFileName()
    {
        return "META-INF/location.properties";
    }

    @Override
    public boolean isOptional()
    {
        return false;
    }
}