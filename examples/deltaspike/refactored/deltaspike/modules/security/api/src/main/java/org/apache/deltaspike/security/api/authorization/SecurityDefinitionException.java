package org.apache.deltaspike.security.api.authorization;

/**
 * This exception is thrown when a security-related configuration error is detected,
 * such as a missing or ambiguous security binding type
 */
public class SecurityDefinitionException extends org.apache.deltaspike.security.api.SecurityException
{
    private static final long serialVersionUID = -5683365417825375411L;

    public SecurityDefinitionException(String message) 
    {
        super(message);
    }

    @SuppressWarnings("UnusedDeclaration")
    public SecurityDefinitionException(Throwable cause)
    {
        super(cause);
    }

    public SecurityDefinitionException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}