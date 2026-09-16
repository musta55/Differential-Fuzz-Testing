package org.apache.deltaspike.example.echo;

/**
 * Interface for the different kinds of echo-services
 */
public interface EchoService
{
    /**
     * Returns the given text again - the format might change
     *
     * @param message given message
     * @return message text
     */
    String echo(String message);
}