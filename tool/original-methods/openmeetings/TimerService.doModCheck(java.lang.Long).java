private void doModCheck(Long roomId) {
    modCheckMap.put(roomId, new CompletableFuture<>().completeAsync(() -> {
        ThreadContext.setApplication(app);
        log.warn("Moderator room check {}", roomId);
        if (cm.streamByRoom(roomId).findAny().isEmpty()) {
            modCheckMap.remove(roomId);
        } else {
            WebSocketHelper.sendRoom(new TextRoomMessage(roomId, sysUser, RoomMessage.Type.MODERATOR_IN_ROOM, "" + !cm.streamByRoom(roomId).filter(c -> c.hasRight(Right.MODERATOR)).findAny().isEmpty()));
            doModCheck(roomId);
        }
        return null;
    }, delayedExecutor(modCheckInterval, TimeUnit.SECONDS)));
}