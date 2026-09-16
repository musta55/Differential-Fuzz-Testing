@Override
public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
    Path<V> p = path.get(singular);
    CriteriaBuilder.In<V> in = builder.in(p);
    for (V value : values) {
        if (value != null) {
            in.value(value);
        }
    }
    return Arrays.asList((Predicate) in);
}