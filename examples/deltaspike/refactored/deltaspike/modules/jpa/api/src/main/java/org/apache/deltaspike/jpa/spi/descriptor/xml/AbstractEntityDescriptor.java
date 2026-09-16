package org.apache.deltaspike.jpa.spi.descriptor.xml;

import java.io.Serializable;

public abstract class AbstractEntityDescriptor
{
    private String[] id;
    private String version;
    private String name;
    private Class<?> entityClass;
    private Class<? extends Serializable> idClass;
    private AbstractEntityDescriptor parent;

    public AbstractEntityDescriptor()
    {
    }
    
    public AbstractEntityDescriptor(String[] id, String version, String name, Class<?> entityClass,
            Class<? extends Serializable> idClass, AbstractEntityDescriptor parent)
    {
        this.id = id;
        this.version = version;
        this.name = name;
        this.entityClass = entityClass;
        this.idClass = idClass;
        this.parent = parent;
    }

    public String[] getId()
    {
        return id;
    }

    public void setId(String[] id)
    {
        this.id = id;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Class<?> getEntityClass()
    {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass)
    {
        this.entityClass = entityClass;
    }

    public Class<? extends Serializable> getIdClass()
    {
        return idClass;
    }

    public void setIdClass(Class<? extends Serializable> idClass)
    {
        this.idClass = idClass;
    }

    public AbstractEntityDescriptor getParent()
    {
        return parent;
    }

    public void setParent(AbstractEntityDescriptor parent)
    {
        this.parent = parent;
    }
}