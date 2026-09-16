/**
 * Get a list of instances of {@link T}
 *
 * @param search - string search criteria to filter entities
 * @param start - the start to range to retrieve
 * @param count - maximum instance count to retrieve
 * @param order - column and sort order
 * @return list of instances in the range specified
 */
default List<T> adminGet(String search, long start, long count, SortParam<String> order) {
    return get(search, start, count, order);
}