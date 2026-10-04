public static RoomFileDTO get(JSONObject o) {
    if (o == null) {
        return null;
    }
    RoomFileDTO rf = new RoomFileDTO();
    populateDtoFromJson(o, rf);
    return rf;
}
// ---- helper method(s) introduced by the refactoring ----
private static void addRoomFilesToDtoList(List<RoomFile> rfl, List<RoomFileDTO> r) {
    for (RoomFile rf : rfl) {
        r.add(new RoomFileDTO(rf));
    }
}

private static void populateDtoFromJson(JSONObject o, RoomFileDTO rf) {
    rf.id = optLong(o, "id");
    rf.fileId = o.getLong("fileId");
    rf.wbIdx = optLong(o, "wbIdx");
}

