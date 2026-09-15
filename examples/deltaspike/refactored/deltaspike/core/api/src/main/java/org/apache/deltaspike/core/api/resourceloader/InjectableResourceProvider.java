package org.apache.deltaspike.core.api.resourceloader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Provides lookup capability to find a resource.
 *
 */
public interface InjectableResourceProvider
{
    InputStream readStream(final InjectableResource injectableResource);

    List<InputStream> readStreams(final InjectableResource injectableResource);

    Properties readProperties(final InjectableResource injectableResource);
}