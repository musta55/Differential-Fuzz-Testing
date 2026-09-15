package org.apache.deltaspike.data.impl.builder;

public class MethodExpressionException extends RuntimeException
{

    private static final long serialVersionUID = 1L;
    private final String property;
    private final Class<?> repoClass;
    private final String method;

    public MethodExpressionException(Class<?> repoClass, String method)
    {
        this(null, repoClass, method);
    }

    public MethodExpressionException(String property, Class<?> repoClass, String method)
    {
        this.property = property;
        this.repoClass = repoClass;
        this.method = method;
    }

    @Override
    public String getMessage()
    {
        if (property != null)
        {
            return "Invalid property '" + property + "' in method expression " +
                   repoClass.getName() + "." + method;
        }
        return "Method '" + method + "' of Repository " +
               repoClass.getName() + " is not a method expression";
    }

}