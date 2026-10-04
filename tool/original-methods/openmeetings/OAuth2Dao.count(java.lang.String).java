@Override
public long count(String search) {
    return DaoHelper.count(em, LdapConfig.class, search, searchFields, true, null);
}