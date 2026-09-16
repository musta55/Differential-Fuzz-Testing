package org.apache.deltaspike.data.impl.meta;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.impl.util.EntityUtils;

@ApplicationScoped
public class EntityMetadataInitializer
{
    private static final Logger LOG = Logger.getLogger(EntityMetadataInitializer.class.getName());
    
    public EntityMetadata init(RepositoryMetadata metadata)
    {
        EntityMetadata entityMetadata = extract(metadata.getRepositoryClass());
        
        entityMetadata.setPrimaryKeyProperty(EntityUtils.primaryKeyProperty(entityMetadata.getEntityClass()));
        entityMetadata.setVersionProperty(EntityUtils.getVersionProperty(entityMetadata.getEntityClass()));
        entityMetadata.setEntityName(EntityUtils.entityName(entityMetadata.getEntityClass()));

        return entityMetadata;
    }
    
    private EntityMetadata extract(Class<?> repositoryClass)
    {
        // get from annotation
        Repository repository = repositoryClass.getAnnotation(Repository.class);
        Class<?> entityClass = repository.forEntity();
        boolean isEntityClass = !Object.class.equals(entityClass) && EntityUtils.isEntityClass(entityClass);
        if (isEntityClass)
        {
            return new EntityMetadata(entityClass, EntityUtils.primaryKeyClass(entityClass));
        }
        
        // get from type
        for (Type inf : repositoryClass.getGenericInterfaces())
        {
            EntityMetadata result = extractFromType(inf);
            if (result != null)
            {
                return result;
            }
        }

        EntityMetadata entityMetadata = extractFromType(repositoryClass.getGenericSuperclass());
        if (entityMetadata != null)
        {
            return entityMetadata;
        }
        for (Type intf : repositoryClass.getGenericInterfaces())
        {
            entityMetadata = extract((Class<?>) intf);
            if (entityMetadata != null)
            {
                return entityMetadata;
            }
        }
        if (repositoryClass.getSuperclass() != null)
        {
            return extract(repositoryClass.getSuperclass());
        }
        return null;
    }

    private EntityMetadata extractFromType(Type type)
    {
        LOG.log(Level.FINER, "extractFrom: type = {0}", type);

        if (!(type instanceof ParameterizedType))
        {
            return null;
        }
        
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] genericTypes = parameterizedType.getActualTypeArguments();
        
        EntityMetadata result = findEntityType(genericTypes);
        if (result != null)
        {
            setPrimaryKeyClass(result, genericTypes);
        }
        return result;
    }

    private EntityMetadata findEntityType(Type[] genericTypes)
    {
        for (Type genericType : genericTypes)
        {
            if (genericType instanceof Class && EntityUtils.isEntityClass((Class<?>) genericType))
            {
                return new EntityMetadata((Class<?>) genericType);
            }
        }
        return null;
    }

    private void setPrimaryKeyClass(EntityMetadata result, Type[] genericTypes)
    {
        for (Type genericType : genericTypes)
        {
            if (genericType instanceof Class)
            {
                result.setPrimaryKeyClass((Class<? extends Serializable>) genericType);
                return;
            }
        }
    }
}