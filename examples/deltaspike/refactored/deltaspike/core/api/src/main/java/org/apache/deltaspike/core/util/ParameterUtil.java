package org.apache.deltaspike.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.enterprise.inject.Typed;

@Typed()
public abstract class ParameterUtil
{
    private static boolean parameterSupported = true;
    private static Class<?> parameterClass;
    private static Method getNameMethod;
    private static Method getParametersMethod;

    static
    {
        try
        {
            parameterClass = Class.forName("java.lang.reflect.Parameter");
            getNameMethod = parameterClass.getMethod("getName");
            getParametersMethod = Method.class.getMethod("getParameters");
        }
        catch (Exception e)
        {
            parameterSupported = false;
            parameterClass = null;
            getNameMethod = null;
            getParametersMethod = null;
        }
    }

    public static boolean isParameterSupported()
    {
        return parameterSupported;
    }

    public static String getName(Method method, int parameterIndex)
    {
        if (!isParameterSupported() || method == null)
        {
            return null;
        }
        try
        {
            Object[] parameters = (Object[]) getParametersMethod.invoke(method);
            return (String) getNameMethod.invoke(parameters[parameterIndex]);
        }
        catch (IllegalAccessException e)
        {
            // Log the IllegalAccessException
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // Log the InvocationTargetException and its cause
            e.printStackTrace();
            if (e.getCause() != null)
            {
                e.getCause().printStackTrace();
            }
        }
        return null;
    }
}