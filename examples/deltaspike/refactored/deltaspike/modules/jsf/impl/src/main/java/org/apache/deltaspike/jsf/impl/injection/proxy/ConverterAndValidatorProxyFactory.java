package org.apache.deltaspike.jsf.impl.injection.proxy;

import org.apache.deltaspike.proxy.api.DeltaSpikeProxyFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Typed;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHolder;

@Typed
public class ConverterAndValidatorProxyFactory extends DeltaSpikeProxyFactory
{
    private static final ConverterAndValidatorProxyFactory INSTANCE = new ConverterAndValidatorProxyFactory();
    
    public static ConverterAndValidatorProxyFactory getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected String getProxyClassSuffix()
    {
        return "$$DSJsfProxy";
    }

    @Override
    protected ArrayList<Method> getDelegateMethods(Class<?> targetClass, ArrayList<Method> allMethods)
    {
        ArrayList<Method> delegateMethods = new ArrayList<>();
        if (!StateHolder.class.isAssignableFrom(targetClass))
        {
            delegateMethods.addAll(Arrays.asList(StateHolder.class.getDeclaredMethods()));
        }
        if (!PartialStateHolder.class.isAssignableFrom(targetClass))
        {
            delegateMethods.addAll(Arrays.asList(PartialStateHolder.class.getDeclaredMethods()));
        }
        return delegateMethods.isEmpty() ? null : delegateMethods;
    }
    
    @Override
    protected Class<?>[] getAdditionalInterfacesToImplement(Class<?> targetClass)
    {
        return Arrays.stream(targetClass.getInterfaces())
                     .filter(itf -> itf != PartialStateHolder.class)
                     .toArray(Class[]::new);
    }
}