package org.apache.deltaspike.core.util;

import java.lang.annotation.Annotation;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.BeanManager;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;

/**
 * A set of utility methods for working with contexts.
 */
@Typed()
public abstract class ContextUtils
{
    private ContextUtils()
    {
        // prevent instantiation
    }

    /**
     * Checks if the context for the given scope annotation is active.
     *
     * @param scopeAnnotationClass The scope annotation (e.g. @RequestScoped.class)
     * @return If the context is active.
     */
    public static boolean isContextActive(Class<? extends Annotation> scopeAnnotationClass)
    {
        return isContextActive(scopeAnnotationClass, BeanManagerProvider.getInstance().getBeanManager());
    }

    /**
     * Checks if the context for the given scope annotation is active.
     *
     * @param scopeAnnotationClass The scope annotation (e.g. @RequestScoped.class)
     * @param beanManager The {@link BeanManager}
     * @return If the context is active.
     */
    public static boolean isContextActive(Class<? extends Annotation> scopeAnnotationClass, BeanManager beanManager)
    {
        return checkContextActivity(scopeAnnotationClass, beanManager);
    }

    private static boolean checkContextActivity(Class<? extends Annotation> scopeAnnotationClass, BeanManager beanManager)
    {
        try
        {
            if (beanManager.getContext(scopeAnnotationClass) == null
                    || !beanManager.getContext(scopeAnnotationClass).isActive())
            {
                return false;
            }
        }
        catch (ContextNotActiveException e)
        {
            return false;
        }

        return true;
    }
}