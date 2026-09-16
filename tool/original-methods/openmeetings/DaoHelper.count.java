public static <T> long count(EntityManager em, Class<T> clazz, BiFunction<CriteriaBuilder, Root<T>, Expression<Long>> queuePath, String search, List<String> searchFields, boolean noDeleted, BiFunction<CriteriaBuilder, CriteriaQuery<?>, Predicate> filter) {
    CriteriaQuery<Long> query = query(em, clazz, Long.class, queuePath, false, search, searchFields, noDeleted, filter, null);
    return em.createQuery(query).getSingleResult();
}