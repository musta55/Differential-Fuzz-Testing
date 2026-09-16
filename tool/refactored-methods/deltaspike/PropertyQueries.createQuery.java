/**
 * Create a new {@link PropertyQuery}
 *
 * @param <V>
 * @param targetClass
 * @return
 */
public static <V> PropertyQuery<V> createQuery(Class<?> targetClass) {
    return new PropertyQuery<>(targetClass);
}