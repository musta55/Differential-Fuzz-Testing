package org.apache.deltaspike.data.impl.util.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

public class PersistenceUnitUtilDelegateFactory
{
    private PersistenceUnitUtilDelegateFactory()
    {
    }

    public static PersistenceUnitUtil get(EntityManager entityManager)
    {
        final EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
        final String vendorName = getVendorName(entityManagerFactory);
        if ("openjpa".equalsIgnoreCase(vendorName))
        {
            return new OpenJpaPersistenceUnitUtilDelegate(entityManager);
        }
        return entityManagerFactory.getPersistenceUnitUtil();
    }

    private static String getVendorName(EntityManagerFactory entityManagerFactory)
    {
        return (String) entityManagerFactory.getProperties().get("VendorName");
    }
}