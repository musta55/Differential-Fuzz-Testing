public List<BaseFileItem> getByParent(BaseFileItem node, Long id) {
    List<BaseFileItem> list = new ArrayList<>();
    if (node instanceof Recording) {
        list.addAll(getRecordingsByParent(node, id));
    } else {
        list.addAll(getFilesByParent(node, id));
    }
    if (node.isReadOnly()) {
        markItemsReadOnly(list);
    }
    return list;
}
// ---- helper method(s) introduced by the refactoring ----
private void addUserSpecificRoots(boolean all, List<BaseFileItem> fRoot, List<BaseFileItem> rRoot) {
    if (all && roomId != null) {
        fRoot.add(createUserFilesRoot());
    }
    if (all && isRecordingsEnabled()) {
        rRoot.add(createMyRecordingsRoot());
        rRoot.add(createPublicRecordingsRoot());
    }
}

private BaseFileItem createUserFilesRoot() {
    BaseFileItem r = createRoot(Application.getString("706"), FILES_MY, false);
    r.setOwnerId(getUserId());
    return r;
}

private BaseFileItem createMyRecordingsRoot() {
    BaseFileItem my = createRoot(Application.getString("860"), RECORDINGS_MY, true);
    my.setOwnerId(getUserId());
    return my;
}

private BaseFileItem createPublicRecordingsRoot() {
    BaseFileItem pub = createRoot(lblPublic, RECORDINGS_PUBLIC, true);
    return pub;
}

private void addRoomSpecificRoots(List<BaseFileItem> fRoot) {
    if (roomId != null) {
        BaseFileItem r = createRoot(Application.getString("707"), FILES_ROOM, false);
        r.setRoomId(roomId);
        fRoot.add(r);
    }
}

private void addGroupSpecificRoots(boolean all, List<BaseFileItem> rRoot, List<BaseFileItem> fRoot) {
    for (GroupUser gu : userDao.get(getUserId()).getGroupUsers()) {
        Group g = gu.getGroup();
        boolean readOnly = g.isRestricted() && !hasAdminLevel(getRights()) && !gu.isModerator();
        if (all && isRecordingsEnabled()) {
            rRoot.add(createGroupRecordingsRoot(g, readOnly));
        }
        fRoot.add(createGroupFilesRoot(g, readOnly));
    }
}

private BaseFileItem createGroupRecordingsRoot(Group g, boolean readOnly) {
    BaseFileItem r = createRoot(String.format("%s (%s)", lblGroupRec, g.getName()), String.format(RECORDINGS_GROUP, g.getId()), true);
    r.setReadOnly(readOnly);
    r.setGroupId(g.getId());
    return r;
}

private BaseFileItem createGroupFilesRoot(Group g, boolean readOnly) {
    BaseFileItem r = createRoot(String.format("%s (%s)", lblGroupFile, g.getName()), String.format(FILES_GROUP, g.getId()), false);
    r.setGroupId(g.getId());
    r.setReadOnly(roomId == null || readOnly);
    return r;
}

private void updateRoots(List<BaseFileItem> fRoot, List<BaseFileItem> rRoot) {
    roots.clear();
    if (roomId == null) {
        roots.addAll(rRoot);
        roots.addAll(fRoot);
    } else {
        roots.addAll(fRoot);
        roots.addAll(rRoot);
    }
}

private List<Recording> getRecordingsByParent(BaseFileItem node, Long id) {
    if (id == null) {
        return node.getOwnerId() == null ? recDao.getRootByPublic(node.getGroupId()) : recDao.getRootByOwner(node.getOwnerId());
    } else {
        return recDao.getByParent(id);
    }
}

private List<FileItem> getFilesByParent(BaseFileItem node, Long id) {
    if (id == null) {
        if (node.getRoomId() != null) {
            return fileDao.getByRoom(node.getRoomId());
        } else if (node.getGroupId() != null) {
            return fileDao.getByGroup(node.getGroupId(), roomId == null ? VIDEO_TYPES : null);
        } else {
            return fileDao.getByOwner(node.getOwnerId());
        }
    } else {
        return fileDao.getByParent(id, roomId == null ? VIDEO_TYPES : null);
    }
}

private void markItemsReadOnly(List<BaseFileItem> list) {
    for (BaseFileItem f : list) {
        f.setReadOnly(true);
    }
}

