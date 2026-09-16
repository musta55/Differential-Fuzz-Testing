@Override
public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
    return Arrays.asList(builder.lessThan(path.get(getAtt()), getValue()));
}