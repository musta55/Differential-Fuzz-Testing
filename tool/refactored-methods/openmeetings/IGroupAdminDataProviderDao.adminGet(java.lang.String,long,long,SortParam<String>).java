/**
 * Get a list of instances of {@link T}
 *
 * @param searchCriteria - string search criteria to filter entities
 * @param startIndex - the start index to range to retrieve
 * @param maxCount - maximum instance count to retrieve
 * @param sortOrder - column and sort order
 * @return list of instances in the range specified
 */
default List<T> adminGet(String searchCriteria, long startIndex, long maxCount, SortParam<String> sortOrder) {
    return get(searchCriteria, startIndex, maxCount, sortOrder);
}