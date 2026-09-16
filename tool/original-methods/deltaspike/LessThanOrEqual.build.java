@Override
public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
    return Arrays.asList(builder.lessThanOrEqualTo(path.get(getAtt()), getValue()));
}