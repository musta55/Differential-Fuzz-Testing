@Override
protected FileItem getFileItem(Attributes attr) {
    PageParameters params = attr.getParameters();
    StringValue idStr = params.get("id");
    String uid = params.get("uid").toString();
    Long id = null;
    try {
        id = idStr.toOptionalLong();
    } catch (NumberFormatException e) {
        //no-op expected
    }
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
    if (isAtWb(c, ruid, wuid, f.getId())) {
        // item IS on WB
        return f;
    }
    if (f.getGroupId() != null && groupUserDao.isUserInGroup(f.getGroupId(), getUserId())) {
        return f;
    }
    return null;
}