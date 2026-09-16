public void updatePoll(IPartialPageRequestHandler handler, Long createdBy) {
    RoomPoll rp = pollDao.getByRoom(room.getRoom().getId());
    if (rp != null) {
        vote.updateModel(handler, rp);
    } else {
        vote.close(handler);
    }
    if (createdBy != null && !getUserId().equals(createdBy)) {
        vote.show(handler);
    }
    if (pollResults.isOpened()) {
        pollResults.updateModel(handler, false, room.getClient().hasRight(Room.Right.MODERATOR));
    }
}