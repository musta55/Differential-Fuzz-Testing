public void refreshRoots(boolean all) {
    List<BaseFileItem> fRoot = new ArrayList<>(), rRoot = new ArrayList<>();
    if (all && roomId != null) {
        BaseFileItem r = createRoot(Application.getString("706"), FILES_MY, false);
        r.setOwnerId(getUserId());
        fRoot.add(r);
    }
    if (roomId != null) {
        BaseFileItem r = createRoot(Application.getString("707"), FILES_ROOM, false);
        r.setRoomId(roomId);
        fRoot.add(r);
    }
    if (all && isRecordingsEnabled()) {
        BaseFileItem my = createRoot(Application.getString("860"), RECORDINGS_MY, true);
        my.setOwnerId(getUserId());
        rRoot.add(my);
        BaseFileItem pub = createRoot(lblPublic, RECORDINGS_PUBLIC, true);
        rRoot.add(pub);
    }
    for (GroupUser gu : userDao.get(getUserId()).getGroupUsers()) {
        Group g = gu.getGroup();
        boolean readOnly = g.isRestricted() && !hasAdminLevel(getRights()) && !gu.isModerator();
        if (all && isRecordingsEnabled()) {
            BaseFileItem r = createRoot(String.format("%s (%s)", lblGroupRec, g.getName()), String.format(RECORDINGS_GROUP, g.getId()), true);
            r.setReadOnly(readOnly);
            r.setGroupId(g.getId());
            rRoot.add(r);
        }
        BaseFileItem r = createRoot(String.format("%s (%s)", lblGroupFile, g.getName()), String.format(FILES_GROUP, g.getId()), false);
        r.setGroupId(g.getId());
        //group videos are read-only in recordings tree
        r.setReadOnly(roomId == null || readOnly);
        fRoot.add(r);
    }
    roots.clear();
    if (roomId == null) {
        roots.addAll(rRoot);
        roots.addAll(fRoot);
    } else {
        roots.addAll(fRoot);
        roots.addAll(rRoot);
    }
}