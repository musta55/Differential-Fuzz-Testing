@Override
protected String getMimeType(FileItem r) {
    String mime = MIME_TYPE_MAP.get(r.getType());
    if (mime == null) {
        throw new RuntimeException("Not supported");
    }
    return r.isDeleted() ? PNG_MIME_TYPE : mime;
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

