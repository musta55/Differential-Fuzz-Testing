package org.apache.deltaspike.data.impl.util.jpa;

import org.apache.deltaspike.data.impl.util.EntityUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OpenJpaPersistenceUnitUtilDelegate implements PersistenceUnitUtil
{
    private final PersistenceUnitUtil persistenceUnitUtil;
    private final EntityManager entityManager;

    public OpenJpaPersistenceUnitUtilDelegate(EntityManager entityManager)
    {
        this.persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        this.entityManager = entityManager;
    }

    @Override
    public boolean isLoaded(Object entity, String attributeName)
    {
        return persistenceUnitUtil.isLoaded(entity, attributeName);
    }

    @Override
    public boolean isLoaded(Object entity)
    {
        return persistenceUnitUtil.isLoaded(entity);
    }

    @Override
    public Object getIdentifier(Object entity)
    {
        final String methodName = "getIdObject";
        try
        {
            if (!entityManager.contains(entity))
            {
                entity = entityManager.getReference(entity.getClass(), EntityUtils.primaryKeyValue(entity));
            }

            Object identifier = persistenceUnitUtil.getIdentifier(entity);
            if (identifier != null)
            {
                Method method = identifier.getClass().getMethod(methodName);
                return method.invoke(identifier);
            }
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalStateException e)
        {
            return null;
        }
        return null;
    }
}