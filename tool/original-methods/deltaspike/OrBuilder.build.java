@Override
public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
    List<Predicate> and = new ArrayList<Predicate>(criteria.length);
    for (Criteria<P, P> c : criteria) {
        and.add(builder.and(c.predicates(builder, path).toArray(new Predicate[0])));
    }
    return Arrays.asList(builder.or(and.toArray(new Predicate[0])));
}