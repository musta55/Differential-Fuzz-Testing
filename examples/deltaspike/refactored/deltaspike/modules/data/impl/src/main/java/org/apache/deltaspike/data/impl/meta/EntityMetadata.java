package org.apache.deltaspike.data.impl.meta;

import java.io.Serializable;
import org.apache.deltaspike.data.impl.property.Property;

public class EntityMetadata
{
    private Class<?> entityClass;
    private Property<Serializable> primaryKeyProperty;
    private Class<? extends Serializable> primaryKeyClass;
    private Property<Serializable> versionProperty;
    private String entityName;

    public EntityMetadata(Class<?> entityClass)
    {
        this(entityClass, null, null, null, null);
    }
    
    public EntityMetadata(Class<?> entityClass,
            Class<? extends Serializable> primaryKeyClass)
    {
        this(entityClass, null, primaryKeyClass, null, null);
    }
    
    public EntityMetadata(Class<?> entityClass, String entityName,
            Class<? extends Serializable> primaryKeyClass)
    {
        this(entityClass, entityName, primaryKeyClass, null, null);
    }

    public EntityMetadata(Class<?> entityClass, String entityName,
            Class<? extends Serializable> primaryKeyClass,
            Property<Serializable> primaryKeyProperty,
            Property<Serializable> versionProperty)
    {
        this.entityClass = entityClass;
        this.entityName = entityName;
        this.primaryKeyClass = primaryKeyClass;
        this.primaryKeyProperty = primaryKeyProperty;
        this.versionProperty = versionProperty;
    }

    public Class<?> getEntityClass()
    {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass)
    {
        this.entityClass = entityClass;
    }

    public Class<? extends Serializable> getPrimaryKeyClass()
    {
        return primaryKeyClass;
    }

    public void setPrimaryKeyClass(Class<? extends Serializable> primaryKeyClass)
    {
        this.primaryKeyClass = primaryKeyClass;
    }

    public Property<Serializable> getPrimaryKeyProperty()
    {
        return primaryKeyProperty;
    }

    public void setPrimaryKeyProperty(Property<Serializable> primaryKeyProperty)
    {
        this.primaryKeyProperty = primaryKeyProperty;
    }

    public Property<Serializable> getVersionProperty()
    {
        return versionProperty;
    }

    public void setVersionProperty(Property<Serializable> versionProperty)
    {
        this.versionProperty = versionProperty;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }
}