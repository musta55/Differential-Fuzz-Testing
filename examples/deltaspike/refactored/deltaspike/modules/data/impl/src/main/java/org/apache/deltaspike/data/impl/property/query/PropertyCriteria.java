package org.apache.deltaspike.data.impl.property.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * A property criteria can be used to filter the properties found by a {@link PropertyQuery}
 * </p>
 * <p/>
 * <p>
 * Solder provides a number of property queries ( {@link TypedPropertyCriteria}, {@link NamedPropertyCriteria} and
 * {@link AnnotatedPropertyCriteria}), or you can create a custom query by implementing this interface.
 * </p>
 *
 * @see PropertyQuery#addCriteria(PropertyCriteria)
 * @see PropertyQueries
 * @see TypedPropertyCriteria
 * @see AnnotatedPropertyCriteria
 * @see NamedPropertyCriteria
 */
public interface PropertyCriteria
{
    /**
     * Tests whether the specified field matches the criteria
     *
     * @param f the field to test
     * @return true if the field matches
     */
    boolean fieldMatches(Field f);

    /**
     * Tests whether the specified method matches the criteria
     *
     * @param m the method to test
     * @return true if the method matches
     */
    boolean methodMatches(Method m);
}