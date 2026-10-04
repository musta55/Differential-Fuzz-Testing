@Override
public final Collection<T> toChoices(Collection<String> inIds) {
    Collection<T> c = new ArrayList<>();
    for (String id : inIds) {
        if (ids.contains(id)) {
            T e = fromId(id);
            if (e != null) {
                c.add(e);
            }
        }
    }
    return c;
}