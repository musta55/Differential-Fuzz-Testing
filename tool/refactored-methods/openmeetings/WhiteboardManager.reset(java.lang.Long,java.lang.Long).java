@Override
public void reset(Long roomId, Long userId) {
    if (roomId == null) {
        return;
    }
    try {
        if (contains(roomId) && map().tryLock(roomId, 1, TimeUnit.SECONDS)) {
            try {
                onlineWbs.remove(roomId);
                map().delete(roomId);
            } finally {
                map().unlock(roomId);
            }
        }
        startResetThread(userId, roomId);
    } catch (InterruptedException e) {
        log.warn("Unexpected exception while map clean-up", e);
        Thread.currentThread().interrupt();
    }
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

