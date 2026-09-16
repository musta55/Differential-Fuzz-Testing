@Override
public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
    return Collections.singletonList(builder.lessThan(path.get(getAtt()), getValue()));
}