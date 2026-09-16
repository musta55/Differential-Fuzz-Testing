@Override
public void updateSizes() {
    RecordingContainerData sizeData = recDao.getContainerData(getUserId());
    long userSize = fileDao.getOwnSize(getUserId());
    long roomSize = fileDao.getRoomSize(room.getRoom().getId());
    if (sizeData != null) {
        userSize += sizeData.getUserHomeSize();
        roomSize += sizeData.getPublicFileSize();
    }
    homeSize.setObject(getHumanSize(userSize));
    publicSize.setObject(getHumanSize(roomSize));
}