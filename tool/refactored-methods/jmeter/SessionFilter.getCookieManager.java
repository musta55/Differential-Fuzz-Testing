@SuppressWarnings("ThreadPriorityCheck")
protected CookieManager getCookieManager(String ipAddr) {
    CookieManager cm;
    synchronized (LOCK) {
        if (lastUsed != null) {
            managersInUse.remove(lastUsed);
            LOCK.notifyAll();
        }
    }
    if (lastUsed != null) {
        Thread.yield();
    }
    synchronized (LOCK) {
        cm = cookieManagers.computeIfAbsent(ipAddr, k -> {
            CookieManager newCm = new CookieManager();
            newCm.testStarted();
            return newCm;
        });
        while (managersInUse.contains(cm)) {
            try {
                LOCK.wait();
            } catch (InterruptedException e) {
                log.info("SessionFilter wait interrupted");
                Thread.currentThread().interrupt();
            }
        }
        managersInUse.add(cm);
        lastUsed = cm;
    }
    return cm;
}