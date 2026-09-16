public RoomFile get(RoomFileDTO dto, Long roomId) {
    RoomFile f = new RoomFile();
    f.setId(dto.getId());
    f.setRoomId(roomId);
    f.setFile(fileDao.getBase(dto.getFileId()));
    f.setWbIdx(dto.getWbIdx());
    return f;
}