@Override
protected FileItem getFileItem(Attributes attr) {
    PageParameters params = attr.getParameters();
    Long id = getIdFromParams(params.get("id"));
    String uid = params.get("uid").toString();
    WebSession ws = WebSession.get();
    Client c = cm.get(uid);
    if (id == null || !ws.isSignedIn() || c == null) {
        return null;
    }
    FileItem f = (FileItem) fileDao.getAny(id);
    if (f == null) {
        return null;
    }
    String ruid = params.get("ruid").toString();
    String wuid = params.get("wuid").toString();
    if (isAtWb(c, ruid, wuid, f.getId()) || isUserInGroup(f, getUserId())) {
        return f;
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private Long getIdFromParams(StringValue idStr) {
    try {
        return idStr.toOptionalLong();
    } catch (NumberFormatException e) {
        return null;
    }
}

private boolean isUserInGroup(FileItem f, Long userId) {
    return f.getGroupId() != null && groupUserDao.isUserInGroup(f.getGroupId(), userId);
}

