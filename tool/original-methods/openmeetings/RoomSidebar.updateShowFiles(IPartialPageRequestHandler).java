private void updateShowFiles(IPartialPageRequestHandler handler) {
    if (room.isInterview()) {
        return;
    }
    showFiles = !room.getRoom().isHidden(RoomElement.FILES) && room.getClient().hasRight(Right.PRESENTER);
    roomFiles.setReadOnly(!showFiles, handler);
}