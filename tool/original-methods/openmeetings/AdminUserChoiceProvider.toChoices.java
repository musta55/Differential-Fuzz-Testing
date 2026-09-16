@Override
public Collection<User> toChoices(Collection<String> inIds) {
    List<Long> ids = new ArrayList<>();
    for (String id : inIds) {
        ids.add(Long.valueOf(id));
    }
    return userDao.get(ids);
}