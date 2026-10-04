private void doModCheck(Long roomId) {
    modCheckMap.put(roomId, new CompletableFuture<>().completeAsync(() -> {
        executeModCheck(roomId);
        return null;
    }, delayedExecutor(modCheckInterval, TimeUnit.SECONDS)));
}
// ---- helper method(s) introduced by the refactoring ----
private void executeModCheck(Long roomId) {
    ThreadContext.setApplication(app);
    log.warn("Moderator room check {}", roomId);
    if (cm.streamByRoom(roomId).findAny().isEmpty()) {
        modCheckMap.remove(roomId);
    } else {
        WebSocketHelper.sendRoom(new TextRoomMessage(roomId, sysUser, RoomMessage.Type.MODERATOR_IN_ROOM, "" + !cm.streamByRoom(roomId).filter(c -> c.hasRight(Right.MODERATOR)).findAny().isEmpty()));
        doModCheck(roomId);
    }
}

private void executeSipCheck(Long roomId) {
    ThreadContext.setApplication(app);
    log.trace("Sip room check {}", roomId);
    Optional<Client> sipClient = cm.streamByRoom(roomId).filter(Client::isSip).findAny();
    cm.streamByRoom(roomId).filter(Predicate.not(Client::isSip)).findAny().ifPresentOrElse(c -> {
        updateSipLastName(sipClient, c.getRoom());
        doSipCheck(roomId);
    }, () -> {
        log.warn("No more clients in the room {}", roomId);
        sipCheckMap.remove(roomId);
        sipClient.ifPresent(cm::exit);
    });
}

