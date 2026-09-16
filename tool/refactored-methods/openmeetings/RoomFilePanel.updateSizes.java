@Override
public void updateSizes() {
    RecordingContainerData sizeData = recDao.getContainerData(getUserId());
    long userSize = calculateUserSize(sizeData);
    long roomSize = calculateRoomSize(sizeData);
    homeSize.setObject(getHumanSize(userSize));
    publicSize.setObject(getHumanSize(roomSize));
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateUserSize(RecordingContainerData sizeData) {
    long userSize = fileDao.getOwnSize(getUserId());
    if (sizeData != null) {
        userSize += sizeData.getUserHomeSize();
    }
    return userSize;
}

private long calculateRoomSize(RecordingContainerData sizeData) {
    long roomSize = fileDao.getRoomSize(room.getRoom().getId());
    if (sizeData != null) {
        roomSize += sizeData.getPublicFileSize();
    }
    return roomSize;
}

