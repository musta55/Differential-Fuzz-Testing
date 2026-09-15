package org.apache.deltaspike.jsf.impl.resource;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;

/**
 * {@link ResourceWrapper} which appends the version of DeltaSpike to the URL.
 */
public class DeltaSpikeResource extends ResourceWrapper
{
    private final Resource wrapped;
    private final String version;

    public DeltaSpikeResource(Resource resource, String version)
    {
        super();
        this.wrapped = resource;
        this.version = version;
    }

    @Override
    public Resource getWrapped()
    {
        return wrapped;
    }

    @Override
    public String getRequestPath()
    {
        return super.getRequestPath() + "&v=" + version;
    }

    @Override
    public String getContentType()
    {
        return wrapped.getContentType();
    }

    @Override
    public String getLibraryName()
    {
        return wrapped.getLibraryName();
    }

    @Override
    public String getResourceName()
    {
        return wrapped.getResourceName();
    }

    @Override
    public void setContentType(String contentType)
    {
        wrapped.setContentType(contentType);
    }

    @Override
    public void setLibraryName(String libraryName)
    {
        wrapped.setLibraryName(libraryName);
    }

    @Override
    public void setResourceName(String resourceName)
    {
        wrapped.setResourceName(resourceName);
    }

    @Override
    public String toString()
    {
        return wrapped.toString();
    }
}