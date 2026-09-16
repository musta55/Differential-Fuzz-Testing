@SuppressWarnings("ThreadPriorityCheck")
protected CookieManager getCookieManager(String ipAddr) {
    CookieManager cm;
    // First have to release the cookie we were using so other
    // threads stuck in wait can move on
    synchronized (LOCK) {
        if (lastUsed != null) {
            managersInUse.remove(lastUsed);
            LOCK.notifyAll();
        }
    }
    // let notified threads move on and get lock on managersInUse
    if (lastUsed != null) {
        Thread.yield();
    }
    // here is the core routine to find appropriate cookie manager and
    // check it's not being used.  If used, wait until whoever's using it gives
    // it up
    synchronized (LOCK) {
        cm = cookieManagers.get(ipAddr);
        if (cm == null) {
            cm = new CookieManager();
            cm.testStarted();
            cookieManagers.put(ipAddr, cm);
        }
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