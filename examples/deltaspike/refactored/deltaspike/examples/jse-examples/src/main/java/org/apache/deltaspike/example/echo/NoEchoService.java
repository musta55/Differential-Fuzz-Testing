package org.apache.deltaspike.example.echo;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * This implementation can't be used as CDI bean
 */
@Exclude
public class NoEchoService implements EchoService
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