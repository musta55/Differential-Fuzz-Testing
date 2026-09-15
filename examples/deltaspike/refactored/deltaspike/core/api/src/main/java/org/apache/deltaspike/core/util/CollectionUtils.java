package org.apache.deltaspike.core.util;

import java.util.Collection;
import javax.enterprise.inject.Typed;

/**
 * A collection of utilities for working with Collections
 */
@Typed
public abstract class CollectionUtils
{
    private CollectionUtils()
    {
        // prevent instantiation
    }

    public static boolean isEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }
}