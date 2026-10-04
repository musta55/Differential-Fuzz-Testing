/**
 * Count the number of instances of {@link T}
 *
 * @param searchCriteria - string search criteria to filter entities
 * @return count of instances satisfying given search criteria
 */
default long adminCount(String searchCriteria) {
    return count(searchCriteria);
}