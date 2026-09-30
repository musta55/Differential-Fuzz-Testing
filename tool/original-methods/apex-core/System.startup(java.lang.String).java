public static void startup(String identifier) {
    synchronized (eventloops) {
        DefaultEventLoop el = eventloops.get(identifier);
        if (el == null) {
            try {
                eventloops.put(identifier, el = DefaultEventLoop.createEventLoop(identifier));
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        }
        el.start();
    }
}