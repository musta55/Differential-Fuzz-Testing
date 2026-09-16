package org.apache.deltaspike.jpa.spi.entitymanager;

import org.apache.deltaspike.core.api.literal.AnyLiteral;
import org.apache.deltaspike.jpa.api.entitymanager.EntityManagerResolver;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class QualifierBackedEntityManagerResolver implements EntityManagerResolver
{
    private final Class<? extends Annotation>[] qualifiers;
    private final BeanManager beanManager;

    public QualifierBackedEntityManagerResolver(BeanManager beanManager, Class<? extends Annotation>... qualifiers)
    {
        this.beanManager = beanManager;
        this.qualifiers = qualifiers;
    }

    @Override
    public EntityManager resolveEntityManager()
    {
        Bean<EntityManager> entityManagerBean = resolveEntityManagerBeans();

        if (entityManagerBean == null)
        {
            throw new IllegalStateException(buildErrorMessage());
        }

        return (EntityManager) beanManager.getReference(entityManagerBean, EntityManager.class,
                beanManager.createCreationalContext(entityManagerBean));
    }

    private String buildErrorMessage() {
        StringBuilder qualifierNames = new StringBuilder();
        for (Class<?> c : qualifiers)
        {
            qualifierNames.append(c.getName()).append(" ");
        }
        return "Cannot find an EntityManager qualified with [" + qualifierNames + "]. Did you add a corresponding producer?";
    }

    private Bean<EntityManager> resolveEntityManagerBeans()
    {
        Set<Bean<?>> entityManagerBeans = beanManager.getBeans(EntityManager.class, new AnyLiteral());
        if (entityManagerBeans.isEmpty())
        {
            return null;
        }

        for (Bean<?> bean : entityManagerBeans)
        {
            for (Class<? extends Annotation> qualifier : qualifiers)
            {
                if (bean.getQualifiers().stream().anyMatch(q -> q.annotationType().equals(qualifier)))
                {
                    return (Bean<EntityManager>) bean;
                }
            }
        }
        return null;
    }
}