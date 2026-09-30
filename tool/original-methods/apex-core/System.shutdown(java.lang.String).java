public static void shutdown(String identifier) {
    synchronized (eventloops) {
        DefaultEventLoop el = eventloops.get(identifier);
        if (el == null) {
            throw new RuntimeException("System with " + identifier + " not setup!");
        } else {
            el.stop();
        }
    }
}