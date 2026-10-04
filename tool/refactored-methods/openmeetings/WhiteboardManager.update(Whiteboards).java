private void update(Whiteboards wbs) {
    onlineWbs.put(wbs.getRoomId(), wbs);
    startUpdateThread(wbs);
}
// ---- helper method(s) introduced by the refactoring ----
private void startResetThread(Long userId, Long roomId) {
    new Thread(() -> {
        ensureApplication();
        User u = new User();
        u.setId(userId);
        WebSocketHelper.sendRoom(new RoomMessage(roomId, u, RoomMessage.Type.WB_RELOAD));
    }).start();
}

private void startUpdateThread(Whiteboards wbs) {
    new Thread(() -> map().put(wbs.getRoomId(), wbs)).start();
}

