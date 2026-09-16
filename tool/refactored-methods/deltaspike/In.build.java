@Override
public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
    Path<V> p = path.get(singular);
    CriteriaBuilder.In<V> in = builder.in(p);
    addNonNullValuesToInClause(in, values);
    return Arrays.asList((Predicate) in);
}
// ---- helper method(s) introduced by the refactoring ----
private void addNonNullValuesToInClause(CriteriaBuilder.In<V> in, V[] values) {
    for (V value : values) {
        if (value != null) {
            in.value(value);
        }
    }
}

