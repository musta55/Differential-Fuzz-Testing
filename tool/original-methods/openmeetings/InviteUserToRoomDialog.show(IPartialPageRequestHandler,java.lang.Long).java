public void show(IPartialPageRequestHandler handler, Long userId) {
    this.userId = userId;
    publicRooms.update(handler, roomDao.getPublicRooms());
    privateRooms.update(handler, getPrivateRooms(getUserId(), userId, roomDao));
    super.show(handler);
}