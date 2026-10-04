@Override
public void run() {
    while (true) {
        try {
            //wait for one second
            Thread.sleep(1000);
            //call garbage collection (just a hint) until garbage collection
            //was actually trigger as indicated by a cleaned weak reference
            WeakReference<int[]> wr = new WeakReference<int[]>(new int[1024]);
            while (wr.get() != null) {
                System.gc();
            }
            long mem = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("MemoryMonitor: " + OptimizerUtils.toMB(mem) + " MB used.");
        } catch (InterruptedException e) {
            throw new DMLRuntimeException(e);
        }
    }
}