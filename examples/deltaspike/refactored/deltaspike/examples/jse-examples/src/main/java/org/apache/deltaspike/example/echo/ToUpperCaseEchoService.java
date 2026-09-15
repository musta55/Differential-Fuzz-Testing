package org.apache.deltaspike.example.echo;

import javax.enterprise.context.ApplicationScoped;

/**
 * Implementation of {@link EchoService} which returns the given text in upper-case format
 */
@ApplicationScoped
public class ToUpperCaseEchoService implements EchoService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String echo(String message)
    {
        return message.toUpperCase();
    }
}