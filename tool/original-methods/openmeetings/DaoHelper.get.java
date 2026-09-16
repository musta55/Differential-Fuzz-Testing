public static <T, R> List<T> get(EntityManager em, Class<R> rootClazz, Class<T> clazz, BiFunction<CriteriaBuilder, Root<R>, Expression<T>> queuePath, boolean distinct, String search, List<String> searchFields, boolean noDeleted, BiFunction<CriteriaBuilder, CriteriaQuery<?>, Predicate> filter, SortParam<String> sort, long start, long count) {
    CriteriaQuery<T> query = query(em, rootClazz, clazz, queuePath, distinct, search, searchFields, noDeleted, filter, sort);
    return setLimits(em.createQuery(query), start, count).getResultList();
}