package org.apache.deltaspike.example.echo;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * Default implementation
 */
@Dependent
@Named("DefaultEchoService")
//will be changed to defaultEchoService by org.apache.deltaspike.example.metadata.NamingConventionAwareMetadataFilter
public class DefaultEchoService implements EchoService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String echo(String message)
    {
        return message;
    }
}