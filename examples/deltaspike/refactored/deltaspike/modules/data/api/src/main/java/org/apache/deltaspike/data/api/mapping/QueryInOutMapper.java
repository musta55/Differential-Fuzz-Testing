package org.apache.deltaspike.data.api.mapping;

import java.util.List;

/**
 * Handles concrete mapping of query results and
 * query input parameters.
 */
public interface QueryInOutMapper<E>
{

    /**
     * Map a single result query.
     * @param result        The query result to map.
     * @return              The mapped result object.
     */
    Object mapResult(E result);

    /**
     * Map a query result list.
     * @param result        The query result list to map.
     * @return              The mapped result. Does not have to be a collection.
     */
    Object mapResultList(List<E> result);

    /**
     * Check if this mapper handles a specific input parameter.
     * @param parameter     The parameter candidate for mapping.
     * @return              {@code true} if the mapper handles the parameter.
     */
    boolean mapsParameter(Object parameter);

    /**
     * Map a query parameter.
     * @param parameter     The parameter to map. It can be assumed that the
     *                      {@link #mapsParameter(Object)} method has been
     *                      called before with this parameter.
     * @return              The mapped result.
     */
    Object mapParameter(Object parameter);

}