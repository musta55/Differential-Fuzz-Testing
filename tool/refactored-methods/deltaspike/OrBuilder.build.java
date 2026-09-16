@Override
public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
    List<Predicate> orPredicates = new ArrayList<>(criteria.length);
    for (Criteria<P, P> c : criteria) {
        orPredicates.add(builder.and(c.predicates(builder, path).toArray(new Predicate[0])));
    }
    return Arrays.asList(builder.or(orPredicates.toArray(new Predicate[0])));
}