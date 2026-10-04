@Override
public Collection<User> toChoices(Collection<String> inIds) {
    List<Long> ids = convertToLong(inIds);
    return userDao.get(ids);
}
// ---- helper method(s) introduced by the refactoring ----
private List<Long> convertToLong(Collection<String> inIds) {
    List<Long> ids = new ArrayList<>();
    for (String id : inIds) {
        ids.add(Long.valueOf(id));
    }
    return ids;
}

