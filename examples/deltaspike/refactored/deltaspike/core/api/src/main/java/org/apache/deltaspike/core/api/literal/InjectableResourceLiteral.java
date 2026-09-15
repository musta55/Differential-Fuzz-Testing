package org.apache.deltaspike.core.api.literal;

import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.apache.deltaspike.core.api.resourceloader.InjectableResourceProvider;

import javax.enterprise.util.AnnotationLiteral;

public class InjectableResourceLiteral extends AnnotationLiteral<InjectableResource> implements InjectableResource
{
    private static final long serialVersionUID = 1705986508118055892L;

    private final Class<? extends InjectableResourceProvider> resourceProvider;
    private final String location;

    public InjectableResourceLiteral(Class<? extends InjectableResourceProvider> resourceProvider, String location)
    {
        this.resourceProvider = resourceProvider;
        this.location = location;
    }

    @Override
    public String location()
    {
        return location;
    }

    @Override
    public Class<? extends InjectableResourceProvider> resourceProvider()
    {
        return resourceProvider;
    }
}