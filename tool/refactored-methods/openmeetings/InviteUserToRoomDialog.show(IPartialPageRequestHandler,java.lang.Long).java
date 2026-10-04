public void show(IPartialPageRequestHandler handler, Long userId) {
    this.userId = userId;
    publicRooms.update(handler, roomDao.getPublicRooms());
    privateRooms.update(handler, getPrivateRooms(getUserId(), userId));
    super.show(handler);
}
// ---- helper method(s) introduced by the refactoring ----
private List<Long> getCommonGroupIds(Long userId1, Long userId2) {
    List<Long> orgIds1 = getGroupIds(userId1);
    List<Long> orgIds2 = getGroupIds(userId2);
    orgIds1.retainAll(orgIds2);
    return orgIds1;
}

private List<Long> getGroupIds(Long userId) {
    List<Long> orgIds = new ArrayList<>();
    for (GroupUser gu : userDao.get(userId).getGroupUsers()) {
        orgIds.add(gu.getGroup().getId());
    }
    return orgIds;
}

private List<Room> getPrivateRooms(Long userId1, Long userId2) {
    List<Room> result = new ArrayList<>();
    for (Long orgId : getCommonGroupIds(userId1, userId2)) {
        result.addAll(roomDao.getGroupRooms(orgId));
    }
    return result;
}

