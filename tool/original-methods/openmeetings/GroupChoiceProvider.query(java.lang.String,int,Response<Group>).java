@Override
public void query(String term, int page, Response<Group> response) {
    long count;
    if (AuthLevelUtil.hasAdminLevel(getRights())) {
        count = groupDao.count(term);
        response.addAll(groupDao.get(term, page * PAGE_SIZE, PAGE_SIZE, null));
    } else if (AuthLevelUtil.hasGroupAdminLevel(getRights())) {
        response.addAll(groupDao.adminGet(term, getUserId(), page * PAGE_SIZE, PAGE_SIZE, null));
        count = groupDao.adminCount(term, getUserId());
    } else {
        User u = userDao.get(getUserId());
        for (GroupUser ou : u.getGroupUsers()) {
            if (Strings.isEmpty(term) || ou.getGroup().getName().toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT))) {
                response.add(ou.getGroup());
            }
        }
        count = u.getGroupUsers().size();
    }
    response.setHasMore(page * PAGE_SIZE + response.getResults().size() < count);
}