public static RoomFileDTO get(JSONObject o) {
    if (o == null) {
        return null;
    }
    RoomFileDTO rf = new RoomFileDTO();
    rf.id = optLong(o, "id");
    rf.fileId = o.getLong("fileId");
    rf.wbIdx = optLong(o, "wbIdx");
    return rf;
}