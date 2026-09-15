package org.apache.deltaspike.jpa.spi.entitymanager;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Optional holder which allows to customize the handling of the {@link EntityManager}.
 * Multiple Entity-Managers with different qualifiers aren't supported.
 * See the data-module for further details.
 */
public interface ActiveEntityManagerHolder extends Serializable
{
    void set(EntityManager entityManager);

    boolean isSet();

    EntityManager get();

    void dispose();
}