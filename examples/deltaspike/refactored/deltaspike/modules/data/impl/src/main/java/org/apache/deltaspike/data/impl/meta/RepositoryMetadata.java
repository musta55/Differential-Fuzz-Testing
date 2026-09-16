package org.apache.deltaspike.data.impl.meta;

import java.lang.reflect.Method;
import java.util.Map;
import org.apache.deltaspike.jpa.impl.entitymanager.EntityManagerMetadata;

public class RepositoryMetadata extends EntityManagerMetadata
{
    private Class<?> repositoryClass;
    private EntityMetadata entityMetadata;
    private Map<Method, RepositoryMethodMetadata> methodsMetadata;

    public RepositoryMetadata(Class<?> repositoryClass)
    {
        this.repositoryClass = repositoryClass;
    }
    
    public RepositoryMetadata(Class<?> repositoryClass, EntityMetadata entityMetadata)
    {
        this.repositoryClass = repositoryClass;
        this.entityMetadata = entityMetadata;
    }
    
    public Class<?> getRepositoryClass()
    {
        return repositoryClass;
    }

    public void setRepositoryClass(Class<?> repositoryClass)
    {
        this.repositoryClass = repositoryClass;
    }

    public EntityMetadata getEntityMetadata()
    {
        return entityMetadata;
    }

    public void setEntityMetadata(EntityMetadata entityMetadata)
    {
        this.entityMetadata = entityMetadata;
    }

    public Map<Method, RepositoryMethodMetadata> getMethodsMetadata()
    {
        return methodsMetadata;
    }

    public void setMethodsMetadata(Map<Method, RepositoryMethodMetadata> methodsMetadata)
    {
        this.methodsMetadata = methodsMetadata;
    }
}