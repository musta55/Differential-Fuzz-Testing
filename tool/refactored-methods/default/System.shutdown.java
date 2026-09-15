public static void shutdown(String identifier) {
    synchronized (eventloops) {
        DefaultEventLoop el = getEventLoopFromMap(identifier);
        if (el == null) {
            throw new RuntimeException("System with " + identifier + " not setup!");
        } else {
            el.stop();
        }
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

