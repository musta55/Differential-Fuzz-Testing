package org.apache.deltaspike.data.spi;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

/**
 * Expose the current query invocation to extensions.
 */
public interface QueryInvocationContext
{
    /**
     * Entity Manager used for the query.
     */
    EntityManager getEntityManager();

    /**
     * The class of the Entity related to the invoked Repository.
     */
    Class<?> getEntityClass();

    /**
     * Given the object parameter is an entity, checks if the entity is
     * persisted or not.
     *
     * @param entity Entity object, non nullable.
     * @return true if the entity is not persisted, false otherwise and if no entity.
     */
    boolean isNew(Object entity);

    /**
     * The type of the repository currently accessed.
     */
    Class<?> getRepositoryClass();

    /**
     * The repository method currently executed.
     */
    Method getMethod();
}