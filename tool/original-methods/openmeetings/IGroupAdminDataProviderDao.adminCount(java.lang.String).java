/**
 * Count the number of instances of {@link T}
 *
 * @param search - string search criteria to filter entities
 * @return count of instances satisfying given search criteria
 */
default long adminCount(String search) {
    return count(search);
}