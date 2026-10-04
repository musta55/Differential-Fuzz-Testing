protected boolean isAtWb(Client c, String wbId, String wbItemId, Long fileId) {
    if (c == null || c.getRoom() == null) {
        return false;
    }
    Whiteboards wbs = wbm.get(c.getRoomId());
    if (Strings.isEmpty(wbItemId) || Strings.isEmpty(wbId) || !wbId.equals(wbs.getUid())) {
        return false;
    }
    for (Entry<Long, Whiteboard> e : wbs.getWhiteboards().entrySet()) {
        JSONObject file = e.getValue().get(wbItemId);
        if (file != null && fileId.equals(file.optLong(ATTR_FILE_ID))) {
            // item IS on WB
            return true;
        }
    }
    return false;
}