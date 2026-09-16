private void doSipCheck(Long roomId) {
    sipCheckMap.put(roomId, new CompletableFuture<>().completeAsync(() -> {
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
        return null;
    }, delayedExecutor(sipCheckInterval, TimeUnit.SECONDS)));
}