package org.apache.deltaspike.jpa.spi.descriptor.xml;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.enterprise.inject.Typed;
import org.apache.deltaspike.core.util.StringUtils;

@Typed
public final class PersistenceUnitDescriptorProvider
{
    private static final PersistenceUnitDescriptorProvider INSTANCE = new PersistenceUnitDescriptorProvider();

    private final PersistenceUnitDescriptorParser persistenceUnitDescriptorParser
        = new PersistenceUnitDescriptorParser();
    
    private List<PersistenceUnitDescriptor> persistenceUnitDescriptors = Collections.emptyList();

    private PersistenceUnitDescriptorProvider()
    {
    }

    public static PersistenceUnitDescriptorProvider getInstance()
    {
        return INSTANCE;
    }

    public void init()
    {
        try
        {
            persistenceUnitDescriptors = persistenceUnitDescriptorParser.readAll();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to parse persitence.xml's", e);
        }
    }

    public PersistenceUnitDescriptor get(String name)
    {        
        for (PersistenceUnitDescriptor unit : persistenceUnitDescriptors)
        {
            if (name.equalsIgnoreCase(unit.getName()))
            {
                return unit;
            }
        }
        return null;
    }

    public boolean isEntity(Class<?> entityClass)
    {        
        return find(entityClass) != null;
    }

    public String[] primaryKeyFields(Class<?> entityClass)
    {        
        return findInHierarchy(entityClass, AbstractEntityDescriptor::getId);
    }

    public String versionField(Class<?> entityClass)
    {        
        return findInHierarchy(entityClass, entity -> StringUtils.isEmpty(entity.getVersion()) ? null : entity.getVersion());
    }

    public Class<?> primaryKeyIdClass(Class<?> entityClass)
    {        
        return findInHierarchy(entityClass, AbstractEntityDescriptor::getIdClass);
    }

    public String entityName(Class<?> entityClass)
    {        
        EntityDescriptor entity = find(entityClass);
        if (entity != null)
        {
            return entity.getName();
        }
        return null;
    }
    
    public String entityTableName(Class<?> entityClass)
    {        
        EntityDescriptor entity = find(entityClass);
        if (entity != null)
        {
            return entity.getTableName();
        }
        return null;
    }

    public EntityDescriptor find(Class<?> entityClass)
    {
        for (PersistenceUnitDescriptor unit : persistenceUnitDescriptors)
        {
            EntityDescriptor entity = find(entityClass, unit);
            if (entity != null)
            {
                return entity;
            }
        }
        return null;
    }
    
    protected EntityDescriptor find(Class<?> entityClass, PersistenceUnitDescriptor descriptor)
    {
        for (EntityDescriptor entity : descriptor.getEntityDescriptors())
        {
            if (entity.getEntityClass().equals(entityClass))
            {
                return entity;
            }
        }
        return null;
    }

    private <T> T findInHierarchy(Class<?> entityClass, java.util.function.Function<AbstractEntityDescriptor, T> extractor)
    {
        EntityDescriptor entity = find(entityClass);
        if (entity != null)
        {
            T result = extractor.apply(entity);
            if (result != null)
            {
                return result;
            }
            
            AbstractEntityDescriptor parent = entity.getParent();
            while (parent != null)
            {
                result = extractor.apply(parent);
                if (result != null)
                {
                    return result;
                }
                
                parent = parent.getParent();
            }
        }
        return null;
    }
}