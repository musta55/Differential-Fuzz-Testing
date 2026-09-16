private static <T, Q> Predicate search(String search, List<String> searchFields, boolean noDeleted, BiFunction<CriteriaBuilder, CriteriaQuery<?>, Predicate> filter, CriteriaBuilder builder, Root<T> root, CriteriaQuery<Q> query) {
    Predicate result = builder.conjunction();
    if (noDeleted) {
        result = builder.and(result, builder.equal(root.get("deleted"), false));
    }
    if (filter != null) {
        result = builder.and(result, filter.apply(builder, query));
    }
    if (!Strings.isEmpty(search)) {
        result = builder.and(result, buildSearchPredicate(search, searchFields, builder, root));
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static <T> CriteriaQuery<Long> createCountQuery(EntityManager em, Class<T> clazz, BiFunction<CriteriaBuilder, Root<T>, Expression<Long>> queuePath, String search, List<String> searchFields, boolean noDeleted, BiFunction<CriteriaBuilder, CriteriaQuery<?>, Predicate> filter) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Long> query = builder.createQuery(Long.class);
    Root<T> root = query.from(clazz);
    query.select(queuePath.apply(builder, root));
    query.where(search(search, searchFields, noDeleted, filter, builder, root, query));
    return query;
}

private static <T, R> CriteriaQuery<T> createQuery(EntityManager em, Class<R> rootClazz, Class<T> clazz, BiFunction<CriteriaBuilder, Root<R>, Expression<T>> queuePath, boolean distinct, String search, List<String> searchFields, boolean noDeleted, BiFunction<CriteriaBuilder, CriteriaQuery<?>, Predicate> filter, SortParam<String> sort) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<T> query = builder.createQuery(clazz);
    Root<R> root = query.from(rootClazz);
    query.select(queuePath.apply(builder, root));
    if (distinct) {
        query.distinct(distinct);
    }
    query.where(search(search, searchFields, noDeleted, filter, builder, root, query));
    sort(sort, builder, root, query);
    return query;
}

private static <T> Predicate buildSearchPredicate(String search, List<String> searchFields, CriteriaBuilder builder, Root<T> root) {
    Predicate[] predicates = Stream.of(search.replace("'", "").replace("\"", "").split(" ")).filter(searchItem -> !searchItem.isEmpty()).map(DaoHelper::getStringParam).flatMap(searchItem -> searchFields.stream().map(col -> like(col, searchItem, builder, root))).toArray(Predicate[]::new);
    return builder.or(predicates);
}

private static <T> Path<String> resolvePath(String col, Path<T> root) {
    String[] cols = col.split("[.]");
    Path<String> colPath = null;
    for (String s : cols) {
        colPath = colPath == null ? root.get(s) : colPath.get(s);
    }
    return colPath;
}

