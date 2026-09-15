package org.apache.deltaspike.jpa.impl.entitymanager;

import javax.persistence.EntityManager;
import org.apache.deltaspike.core.api.provider.DependentProvider;
import org.apache.deltaspike.jpa.api.entitymanager.EntityManagerResolver;

public class EntityManagerRef
{
    private EntityManager entityManager;
    private DependentProvider<? extends EntityManager> entityManagerDependentProvider;
    
    private Class<? extends EntityManagerResolver> entityManagerResolverClass;
    private EntityManagerResolver entityManagerResolver;
    private DependentProvider<? extends EntityManagerResolver> entityManagerResolverDependentProvider;
        
    public void release()
    {
        destroyDependentProvider(entityManagerDependentProvider);
        destroyDependentProvider(entityManagerResolverDependentProvider);
    }

    private void destroyDependentProvider(DependentProvider<?> provider) {
        if (provider != null) {
            provider.destroy();
        }
    }

    public Class<? extends EntityManagerResolver> getEntityManagerResolverClass()
    {
        return entityManagerResolverClass;
    }

    public void setEntityManagerResolverClass(Class<? extends EntityManagerResolver> entityManagerResolverClass)
    {
        this.entityManagerResolverClass = entityManagerResolverClass;
    }

    public DependentProvider<? extends EntityManagerResolver> getEntityManagerResolverDependentProvider()
    {
        return entityManagerResolverDependentProvider;
    }

    public void setEntityManagerResolverDependentProvider(
            DependentProvider<? extends EntityManagerResolver> entityManagerResolverDependentProvider)
    {
        this.entityManagerResolverDependentProvider = entityManagerResolverDependentProvider;
    }

    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public EntityManagerResolver getEntityManagerResolver()
    {
        return entityManagerResolver;
    }

    public void setEntityManagerResolver(EntityManagerResolver entityManagerResolver)
    {
        this.entityManagerResolver = entityManagerResolver;
    }

    public DependentProvider<? extends EntityManager> getEntityManagerDependentProvider()
    {
        return entityManagerDependentProvider;
    }

    public void setEntityManagerDependentProvider(
            DependentProvider<? extends EntityManager> entityManagerDependentProvider)
    {
        this.entityManagerDependentProvider = entityManagerDependentProvider;
    }
}