public static <T, Q> Predicate search(String search, List<String> searchFields, boolean noDeleted, BiFunction<CriteriaBuilder, CriteriaQuery<?>, Predicate> filter, CriteriaBuilder builder, Root<T> root, CriteriaQuery<Q> query) {
    Predicate result = builder.isNull(null);
    if (noDeleted) {
        result = builder.and(result, builder.equal(root.get("deleted"), false));
    }
    if (filter != null) {
        result = builder.and(result, filter.apply(builder, query));
    }
    if (!Strings.isEmpty(search)) {
        Predicate[] criterias = Stream.of(search.replace("\'", "").replace("\"", "").split(" ")).filter(searchItem -> !searchItem.isEmpty()).map(DaoHelper::getStringParam).flatMap(searchItem -> searchFields.stream().map(col -> like(col, searchItem, builder, root))).toArray(Predicate[]::new);
        result = builder.and(result, builder.or(criterias));
    }
    return result;
}