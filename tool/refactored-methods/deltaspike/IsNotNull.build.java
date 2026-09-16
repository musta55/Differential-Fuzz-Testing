@Override
public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
    return Collections.singletonList(builder.isNotNull(path.get(getAtt())));
}