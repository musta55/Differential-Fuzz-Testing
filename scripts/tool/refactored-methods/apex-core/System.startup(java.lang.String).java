public static void startup(String identifier) {
    synchronized (eventloops) {
        DefaultEventLoop el = eventloops.get(identifier);
        if (el == null) {
            el = createAndStoreEventLoop(identifier);
        }
        el.start();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static DefaultEventLoop createAndStoreEventLoop(String identifier) {
    try {
        DefaultEventLoop el = DefaultEventLoop.createEventLoop(identifier);
        eventloops.put(identifier, el);
        return el;
    } catch (IOException io) {
        throw new RuntimeException(io);
    }
}

private static DefaultEventLoop getEventLoopFromMap(String identifier) {
    return eventloops.get(identifier);
}

