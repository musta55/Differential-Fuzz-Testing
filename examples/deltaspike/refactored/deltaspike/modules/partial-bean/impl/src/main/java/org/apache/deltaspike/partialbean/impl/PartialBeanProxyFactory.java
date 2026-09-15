package org.apache.deltaspike.partialbean.impl;

import org.apache.deltaspike.proxy.api.DeltaSpikeProxyFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.enterprise.inject.Typed;

/**
 * {@link DeltaSpikeProxyFactory} which delegates all abstract methods to the 
 * partial bean binding {@link java.lang.reflect.InvocationHandler}.
 */
@Typed
public class PartialBeanProxyFactory extends DeltaSpikeProxyFactory
{
    private static final PartialBeanProxyFactory INSTANCE = new PartialBeanProxyFactory();
    
    public static PartialBeanProxyFactory getInstance()
    {
        return INSTANCE;
    }
    
    @Override
    protected String getProxyClassSuffix()
    {
        return "$$DSPartialBeanProxy";
    }

    @Override
    protected ArrayList<Method> getDelegateMethods(Class<?> targetClass, ArrayList<Method> allMethods)
    {
        ArrayList<Method> methods = new ArrayList<>();
        
        for (Method method : allMethods)
        {
            if (Modifier.isAbstract(method.getModifiers()))
            {
                methods.add(method);
            }
        }
        
        return methods;
    }
}