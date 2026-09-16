package org.apache.deltaspike.core.util;

import javax.enterprise.inject.Typed;
import java.lang.reflect.Constructor;

@Typed()
public abstract class ExceptionUtils
{
    private ExceptionUtils()
    {
        // prevent instantiation
    }

    public static RuntimeException throwAsRuntimeException(Throwable throwable)
    {
        //Attention: helper which allows to use a trick to throw
        // a catched checked exception without a wrapping exception
        new ExceptionHelper<RuntimeException>().throwException(throwable);
        return null; //not needed due to the helper trick, but it's easier for using it
    }

    public static void changeAndThrowException(Throwable throwable, String customMessage)
    {
        Throwable newThrowable = createNewException(throwable, customMessage);
        //Attention: helper which allows to use a trick to throw a cached checked exception without a wrapping exception
        new ExceptionHelper<RuntimeException>().throwException(newThrowable);
    }

    private static Throwable createNewException(Throwable throwable, String message)
    {
        Class<? extends Throwable> throwableClass = throwable.getClass();

        try
        {
            Constructor<? extends Throwable> constructor = throwableClass.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            Throwable result = constructor.newInstance(message);
            result.initCause(throwable.getCause());
            return result;
        }
        catch (Exception e)
        {
            //use the original exception if there is any issue
            return throwable;
        }
    }

    @SuppressWarnings({ "unchecked" })
    private static class ExceptionHelper<T extends Throwable>
    {
        private void throwException(Throwable exception) throws T
        {
            try
            {
                //exception-type is only checked at compile-time
                throw (T) exception;
            }
            catch (ClassCastException e)
            {
                handleClassCastException(exception, e);
            }
        }

        private void handleClassCastException(Throwable exception, ClassCastException e)
        {
            //doesn't happen with existing JVMs! - if that changes the local ClassCastException needs to be ignored
            //-> throw original exception
            if (e.getStackTrace()[0].toString().contains(getClass().getName()))
            {
                if (exception instanceof RuntimeException)
                {
                    throw (RuntimeException) exception;
                }
                throw new RuntimeException(exception);
            }
            //if the exception to throw is a ClassCastException, throw it
            throw e;
        }
    }
}