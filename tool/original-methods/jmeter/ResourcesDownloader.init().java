private void init() {
    LOG.info("Creating ResourcesDownloader with keepalive_inseconds : {}", THREAD_KEEP_ALIVE_TIME);
    concurrentExecutor = new ThreadPoolExecutor(MIN_POOL_SIZE, MAX_POOL_SIZE, THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> {
        Thread t = new Thread(r);
        //$NON-NLS-1$
        t.setName("ResDownload-" + t.getName());
        t.setDaemon(true);
        return t;
    }) {
    };
}