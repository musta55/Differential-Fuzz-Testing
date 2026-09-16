@Override
public User fromId(String id) {
    User u;
    if (newContacts.containsKey(id)) {
        u = newContacts.get(id);
    } else {
        u = userDao.get(Long.valueOf(id));
    }
    return u;
}