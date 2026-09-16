@Override
public long size() {
    return hasGroupAdminLevel(getRights()) ? getDao().adminCount(search, getUserId()) : getDao().adminCount(search);
}