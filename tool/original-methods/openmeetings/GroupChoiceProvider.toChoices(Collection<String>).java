@Override
public Collection<Group> toChoices(Collection<String> ids) {
    Collection<Group> c = new ArrayList<>();
    for (String id : ids) {
        c.add(groupDao.get(Long.valueOf(id)));
    }
    return c;
}