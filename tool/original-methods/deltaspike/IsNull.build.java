@Override
public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
    return Arrays.asList(builder.isNull(path.get(getAtt())));
}