@Override
public void run() {
    while (true) {
        try {
            //wait for one second
            Thread.sleep(1000);
            //call garbage collection
            forceGarbageCollection();
            long mem = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("MemoryMonitor: " + OptimizerUtils.toMB(mem) + " MB used.");
        } catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
            throw new DMLRuntimeException(e);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void forceGarbageCollection() {
    //call garbage collection (just a hint) until garbage collection
    //was actually triggered as indicated by a cleaned weak reference
    WeakReference<int[]> wr = new WeakReference<>(new int[1024]);
    while (wr.get() != null) {
        System.gc();
    }
}

