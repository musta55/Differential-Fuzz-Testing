package org.apache.deltaspike.partialbean.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.HashSet;
import java.util.Set;

public class PartialBeanDescriptor
{
    private Class<? extends Annotation> binding;
    private Class<? extends InvocationHandler> handler;
    private Set<Class<?>> classes;

    public PartialBeanDescriptor(Class<? extends Annotation> binding)
    {
        this(binding, null, null);
    }

    public PartialBeanDescriptor(Class<? extends Annotation> binding,
            Class<? extends InvocationHandler> handler)
    {
        this(binding, handler, null);
    }

    public PartialBeanDescriptor(Class<? extends Annotation> binding,
            Class<? extends InvocationHandler> handler,
            Class<?> clazz)
    {
        this.binding = binding;
        this.handler = handler;
        this.classes = new HashSet<>();
        if (clazz != null) {
            this.classes.add(clazz);
        }
    }
    
    public Class<? extends Annotation> getBinding()
    {
        return binding;
    }

    public void setBinding(Class<? extends Annotation> binding)
    {
        this.binding = binding;
    }

    public Class<? extends InvocationHandler> getHandler()
    {
        return handler;
    }

    public void setHandler(Class<? extends InvocationHandler> handler)
    {
        this.handler = handler;
    }

    public Set<Class<?>> getClasses()
    {
        return classes;
    }

    public void setClasses(Set<Class<?>> classes)
    {
        this.classes = classes;
    }
}