public static void reset() {
    if (!refCtr.isEmpty()) {
        System.err.println("There are some dangling streams still in the cache: " + refCtr);
        for (CachingStream cache : refCtr.keySet()) {
            try {
                cache.scheduleDeletion();
            } catch (Exception ex) {
                System.err.println("Failed to schedule deletion for dangling stream " + cache + ": " + ex.getMessage());
            }
        }
        refCtr.clear();
    }
}