@Override
public Iterator<? extends T> iterator(long first, long count) {
    return (hasGroupAdminLevel(getRights()) ? getDao().adminGet(search, getUserId(), first, count, getSort()) : getDao().adminGet(search, first, count, getSort())).iterator();
}