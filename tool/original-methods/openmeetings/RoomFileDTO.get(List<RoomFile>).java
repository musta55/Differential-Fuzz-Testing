public static List<RoomFileDTO> get(List<RoomFile> rfl) {
    List<RoomFileDTO> r = new ArrayList<>();
    if (rfl != null) {
        for (RoomFile rf : rfl) {
            r.add(new RoomFileDTO(rf));
        }
    }
    return r;
}