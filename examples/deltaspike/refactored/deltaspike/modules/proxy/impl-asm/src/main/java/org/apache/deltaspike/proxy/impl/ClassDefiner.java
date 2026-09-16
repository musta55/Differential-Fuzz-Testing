package org.apache.deltaspike.proxy.impl;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

class ClassDefiner
{
    private static final Method CLASS_LOADER_DEFINE_CLASS;
    private static final Method GET_MODULE;
    private static final Method CAN_READ;
    private static final Method ADD_READS;
    private static final Method PRIVATE_LOOKUP_IN;
    private static final Method DEFINE_CLASS;

    static
    {
        Method classLoaderDefineClass = null;
        try
        {
            java.lang.reflect.Method method = ClassLoader.class.getDeclaredMethod(
                "defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
            method.setAccessible(true);
            classLoaderDefineClass = method;
        }
        catch (NoSuchMethodException | SecurityException ignored)
        {
        }
        CLASS_LOADER_DEFINE_CLASS = classLoaderDefineClass;

        Method getModule = null;
        Method canRead = null;
        Method addReads = null;
        Method privateLookupIn = null;
        Method defineClass = null;
        try
        {
            getModule = Class.class.getMethod("getModule");
            Class<?> moduleClass = getModule.getReturnType();
            canRead = moduleClass.getMethod("canRead", moduleClass);
            addReads = moduleClass.getMethod("addReads", moduleClass);
            privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
            defineClass = MethodHandles.Lookup.class.getMethod("defineClass", byte[].class);
        }
        catch (NoSuchMethodException | SecurityException ignored)
        {
        }
        GET_MODULE = getModule;
        CAN_READ = canRead;
        ADD_READS = addReads;
        PRIVATE_LOOKUP_IN = privateLookupIn;
        DEFINE_CLASS = defineClass;
    }

    private ClassDefiner()
    {

    }

    static Class<?> defineClass(ClassLoader loader, String className, byte[] b,
            Class<?> originalClass, ProtectionDomain protectionDomain)
    {
        if (CLASS_LOADER_DEFINE_CLASS == null)
        {
            return defineClassMethodHandles(loader, className, b, originalClass, protectionDomain);
        }
        else
        {
            return defineClassClassLoader(loader, className, b, originalClass, protectionDomain);
        }
    }
    /**
     * Adapted from http://asm.ow2.org/doc/faq.html#Q5
     *
     * @param b
     *
     * @return Class<?>
     */
    static Class<?> defineClassClassLoader(ClassLoader loader, String className, byte[] b,
                                Class<?> originalClass, ProtectionDomain protectionDomain)
    {
        try
        {
            return (Class<?>) CLASS_LOADER_DEFINE_CLASS.invoke(
                loader, className, b, 0, b.length, protectionDomain);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Implementation based on MethodHandles.Lookup.
     *
     * @return Class<?>
     */
    static Class<?> defineClassMethodHandles(ClassLoader loader, String className, byte[] b,
                                             Class<?> originalClass, ProtectionDomain protectionDomain)
    {
        try
        {
            Object thisModule = GET_MODULE.invoke(AsmDeltaSpikeProxyClassGenerator.class);
            Object lookupClassModule = GET_MODULE.invoke(originalClass);
            if (!(boolean) CAN_READ.invoke(thisModule, lookupClassModule))
            {
                // we need to read the other module in order to have privateLookup access
                // see javadoc for MethodHandles.privateLookupIn()
                ADD_READS.invoke(thisModule, lookupClassModule);
            }
            Object lookup = PRIVATE_LOOKUP_IN.invoke(null, originalClass, MethodHandles.lookup());
            return (Class<?>) DEFINE_CLASS.invoke(lookup, b);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}