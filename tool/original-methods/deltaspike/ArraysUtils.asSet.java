/**
 * Create a set from an array. If the array contains duplicate objects, the
 * last object in the array will be placed in resultant set.
 *
 * @param <T>   the type of the objects in the set
 * @param array the array from which to create the set
 * @return the created sets
 */
public static <T> Set<T> asSet(T... array) {
    Set<T> result = new HashSet<T>();
    Collections.addAll(result, array);
    return result;
}