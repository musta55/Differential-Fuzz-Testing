@Override
public void query(String term, int page, Response<Group> response) {
    long count = getCountForRole(term, page, response);
    response.setHasMore(page * PAGE_SIZE + response.getResults().size() < count);
}
// ---- helper method(s) introduced by the refactoring ----
private long getCountForRole(String term, int page, Response<Group> response) {
    if (AuthLevelUtil.hasAdminLevel(getRights())) {
        return handleAdminQuery(term, page, response);
    } else if (AuthLevelUtil.hasGroupAdminLevel(getRights())) {
        return handleGroupAdminQuery(term, page, response);
    } else {
        return handleUserQuery(term, response);
    }
}

private long handleAdminQuery(String term, int page, Response<Group> response) {
    long count = groupDao.count(term);
    response.addAll(groupDao.get(term, page * PAGE_SIZE, PAGE_SIZE, null));
    return count;
}

private long handleGroupAdminQuery(String term, int page, Response<Group> response) {
    long userId = getUserId();
    response.addAll(groupDao.adminGet(term, userId, page * PAGE_SIZE, PAGE_SIZE, null));
    return groupDao.adminCount(term, userId);
}

private long handleUserQuery(String term, Response<Group> response) {
    User user = userDao.get(getUserId());
    user.getGroupUsers().stream().filter(ou -> Strings.isEmpty(term) || matchesTerm(ou.getGroup().getName(), term)).map(GroupUser::getGroup).forEach(response::add);
    return user.getGroupUsers().size();
}

private boolean matchesTerm(String groupName, String term) {
    return groupName.toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT));
}

