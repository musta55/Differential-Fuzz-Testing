package org.apache.deltaspike.jpa.impl.entitymanager;

import org.apache.deltaspike.jpa.api.entitymanager.EntityManagerConfig;
import org.apache.deltaspike.jpa.api.entitymanager.EntityManagerResolver;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.FlushModeType;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

public class EntityManagerMetadata
{
    private Class<? extends EntityManagerResolver> entityManagerResolverClass;
    private EntityManagerResolver unmanagedResolver;
    private Class<? extends Annotation>[] qualifiers;
    private boolean entityManagerResolverIsNormalScope;
    private FlushModeType entityManagerFlushMode;
    private boolean readOnly = false;

    public Class<? extends EntityManagerResolver> getEntityManagerResolverClass()
    {
        return entityManagerResolverClass;
    }

    public void setEntityManagerResolverClass(Class<? extends EntityManagerResolver> entityManagerResolverClass)
    {
        this.entityManagerResolverClass = entityManagerResolverClass;
    }

    public FlushModeType getEntityManagerFlushMode()
    {
        return entityManagerFlushMode;
    }

    public void setEntityManagerFlushMode(FlushModeType entityManagerFlushMode)
    {
        this.entityManagerFlushMode = entityManagerFlushMode;
    }

    public boolean isEntityManagerResolverIsNormalScope()
    {
        return entityManagerResolverIsNormalScope;
    }

    public void setEntityManagerResolverIsNormalScope(boolean entityManagerResolverIsNormalScope)
    {
        this.entityManagerResolverIsNormalScope = entityManagerResolverIsNormalScope;
    }

    public Class<? extends Annotation>[] getQualifiers()
    {
        return qualifiers;
    }

    public void setQualifiers(Class<? extends Annotation>[] qualifiers)
    {
        this.qualifiers = qualifiers;
    }

    public EntityManagerResolver getUnmanagedResolver()
    {
        return unmanagedResolver;
    }

    public void setUnmanagedResolver(EntityManagerResolver unmanagedResolver)
    {
        this.unmanagedResolver = unmanagedResolver;
    }

    public boolean readFrom(AnnotatedElement method, BeanManager beanManager)
    {
        EntityManagerConfig entityManagerConfig = method.getAnnotation(EntityManagerConfig.class);
        boolean processed = handleEntityManagerConfig(beanManager, entityManagerConfig);

        Transactional transactional = method.getAnnotation(Transactional.class);

        processed = handleTransactional(transactional, processed);

        return processed;
    }

    private boolean handleTransactional(Transactional transactional, boolean processed)
    {
        if (transactional != null)
        {
            processed = updateQualifiersFromTransactional(transactional, processed);
            this.readOnly = transactional.readOnly();
        }
        return processed;
    }

    private boolean updateQualifiersFromTransactional(Transactional transactional, boolean processed)
    {
        if (this.qualifiers == null)
        {
            processed = true;
            this.setQualifiers(transactional.qualifier());
        }
        return processed;
    }

    private boolean handleEntityManagerConfig(BeanManager beanManager, EntityManagerConfig entityManagerConfig)
    {
        boolean processed = false;
        if (entityManagerConfig != null)
        {
            processed = true;
            updateEntityManagerConfigFields(entityManagerConfig);
            updateEntityManagerResolver(beanManager, entityManagerConfig);
        }
        return processed;
    }

    private void updateEntityManagerConfigFields(EntityManagerConfig entityManagerConfig)
    {
        this.setEntityManagerFlushMode(entityManagerConfig.flushMode());
        this.setQualifiers(entityManagerConfig.qualifier());
    }

    private void updateEntityManagerResolver(BeanManager beanManager, EntityManagerConfig entityManagerConfig)
    {
        Class<? extends EntityManagerResolver> resolverClass = entityManagerConfig.entityManagerResolver();
        if (!resolverClass.equals(EntityManagerResolver.class))
        {
            this.setEntityManagerResolverClass(resolverClass);
            determineResolverScope(beanManager, resolverClass);
        }
        else
        {
            this.setEntityManagerResolverIsNormalScope(false);
        }
    }

    private void determineResolverScope(BeanManager beanManager, Class<? extends EntityManagerResolver> resolverClass)
    {
        Set<Bean<?>> beans = beanManager.getBeans(resolverClass);
        Class<? extends Annotation> scope = beanManager.resolve(beans).getScope();
        this.setEntityManagerResolverIsNormalScope(beanManager.isNormalScope(scope));
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }
}