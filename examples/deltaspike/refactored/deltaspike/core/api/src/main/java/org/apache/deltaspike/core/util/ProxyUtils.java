package org.apache.deltaspike.core.util;

import javax.enterprise.inject.Typed;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper for proxies
 */
@Typed()
public abstract class ProxyUtils
{
    private ProxyUtils()
    {
        // prevent instantiation
    }

    /**
     * @param currentClass current class
     * @return class of the real implementation
     */
    public static Class getUnproxiedClass(Class currentClass)
    {
        while (isProxiedClass(currentClass))
        {
            currentClass = currentClass.getSuperclass();
        }

        return currentClass;
    }

    /**
     * Analyses if the given class is a generated proxy class
     * @param currentClass current class
     * @return true if the given class is a known proxy class, false otherwise
     */
    public static boolean isProxiedClass(Class currentClass)
    {
        if (currentClass == null || currentClass.getSuperclass() == null)
        {
            return false;
        }

        String name = currentClass.getName();
        return name.startsWith(currentClass.getSuperclass().getName()) &&
               (name.contains("$$") || name.contains("_ClientProxy") || name.contains("$HibernateProxy$"));
    }

    public static List<Class<?>> getProxyAndBaseTypes(Class<?> proxyClass)
    {
        List<Class<?>> result = new ArrayList<>();
        result.add(proxyClass);

        if (isInterfaceProxy(proxyClass))
        {
            for (Class<?> currentInterface : proxyClass.getInterfaces())
            {
                if (proxyClass.getName().startsWith(currentInterface.getName()))
                {
                    result.add(currentInterface);
                }
            }
        }
        else
        {
            addBaseTypes(result, proxyClass);
        }

        return result;
    }

    private static void addBaseTypes(List<Class<?>> result, Class<?> proxyClass)
    {
        Class<?> unproxiedClass = proxyClass.getSuperclass();
        result.add(unproxiedClass);

        while (isProxiedClass(unproxiedClass))
        {
            unproxiedClass = unproxiedClass.getSuperclass();
            result.add(unproxiedClass);
        }
    }

    public static boolean isInterfaceProxy(Class<?> proxyClass)
    {
        Class<?>[] interfaces = proxyClass.getInterfaces();
        if (Proxy.class.equals(proxyClass.getSuperclass()) && interfaces != null && interfaces.length > 0)
        {
            return true;
        }

        if (!Object.class.equals(proxyClass.getSuperclass()))
        {
            return false;
        }

        return proxyClass.getName().contains("$$") && hasMatchingInterface(proxyClass, interfaces);
    }

    private static boolean hasMatchingInterface(Class<?> proxyClass, Class<?>[] interfaces)
    {
        for (Class<?> currentInterface : interfaces)
        {
            if (proxyClass.getName().startsWith(currentInterface.getName()))
            {
                return true;
            }
        }
        return false;
    }
}