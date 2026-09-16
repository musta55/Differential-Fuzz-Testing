package org.apache.deltaspike.security.impl.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.util.Nonbinding;

import org.apache.deltaspike.security.api.authorization.SecurityDefinitionException;

class AuthorizationParameter
{
    private Type type;
    private Map<Class<? extends Annotation>, Map<Method, Object>> bindings;

    public AuthorizationParameter()
    {
    }

    AuthorizationParameter(Type type, Set<Annotation> bindings)
    {
        this.type = type;
        initializeBindings(bindings);
    }

    private void initializeBindings(Set<Annotation> bindings)
    {
        this.bindings = new HashMap<>();
        for (Annotation bindingAnnotation : bindings)
        {
            Map<Method, Object> bindingMembers = extractBindingMembers(bindingAnnotation);
            this.bindings.put(bindingAnnotation.annotationType(), bindingMembers);
        }
    }

    private Map<Method, Object> extractBindingMembers(Annotation bindingAnnotation)
    {
        Map<Method, Object> bindingMembers = new HashMap<>();
        try
        {
            for (Method method : bindingAnnotation.annotationType().getDeclaredMethods())
            {
                if (method.isAnnotationPresent(Nonbinding.class))
                {
                    continue;
                }
                bindingMembers.put(method, method.invoke(bindingAnnotation));
            }
        }
        catch (InvocationTargetException | IllegalAccessException ex)
        {
            throw new SecurityDefinitionException("Error reading security binding members", ex);
        }
        return bindingMembers;
    }

    /**
     * TODO comment is no equals!!!
     * 
     * @param parameter
     * @return
     */
    boolean matches(AuthorizationParameter parameter)
    {
        if (!type.equals(parameter.type))
        {
            return false;
        }
        return matchBindings(parameter);
    }

    private boolean matchBindings(AuthorizationParameter parameter)
    {
        for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> bindingEntry : bindings.entrySet())
        {
            Map<Method, Object> bindingValues = parameter.bindings.get(bindingEntry.getKey());
            if (bindingValues == null || !matchBindingValues(bindingValues, bindingEntry.getValue()))
            {
                return false;
            }
        }
        return true;
    }

    private boolean matchBindingValues(Map<Method, Object> bindingValues, Map<Method, Object> expectedValues)
    {
        for (Map.Entry<Method, Object> entry : expectedValues.entrySet())
        {
            if (!bindingValues.get(entry.getKey()).equals(entry.getValue()))
            {
                return false;
            }
        }
        return true;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        appendBindings(builder);
        appendType(builder);
        return builder.toString();
    }

    private void appendBindings(StringBuilder builder)
    {
        for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> bindingEntry : bindings.entrySet())
        {
            builder.append('@').append(bindingEntry.getKey().getName()).append('(');
            appendBindingValues(builder, bindingEntry.getValue());
            builder.append(' ');
        }
    }

    private void appendBindingValues(StringBuilder builder, Map<Method, Object> values)
    {
        for (Map.Entry<Method, Object> entry : values.entrySet())
        {
            builder.append(entry.getKey().getName()).append('=').append(entry.getValue()).append(',');
        }
        if (!values.isEmpty())
        {
            builder.setCharAt(builder.length() - 1, ')');
        }
    }

    private void appendType(StringBuilder builder)
    {
        if (!bindings.isEmpty())
        {
            builder.append(' ');
        }
        builder.append(type);
    }
}