@Override
public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
    return Collections.singletonList(builder.lessThanOrEqualTo(path.get(getAtt()), getValue()));
}