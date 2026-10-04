/**
 * Listen on the daemon port and handle incoming requests. This method will
 * not exit until {@link #stopServer()} is called or an error occurs.
 */
@Override
public void run() {
    running = true;
    log.info("Test Script Recorder up and running!");
    // Maps to contain page and form encodings
    Map<String, String> pageEncodings = Collections.synchronizedMap(new HashMap<>());
    Map<String, String> formEncodings = Collections.synchronizedMap(new HashMap<>());
    pageEncodings.put(AbstractSamplerCreator.DEFAULT_ENCODING_KEY, target.getDefaultEncoding());
    try {
        while (running) {
            try {
                // Listen on main socket
                Socket clientSocket = mainSocket.accept();
                if (running) {
                    // Pass request to new proxy thread
                    Proxy thd = proxyClass.getDeclaredConstructor().newInstance();
                    thd.configure(clientSocket, target, pageEncodings, formEncodings);
                    thd.start();
                }
            } catch (InterruptedIOException ignored) {
                // Timeout occurred. Ignore, and keep looping until we're
                // told to stop running.
            }
        }
        log.info("HTTP(S) Test Script Recorder stopped");
    } catch (Exception e) {
        log.warn("HTTP(S) Test Script Recorder stopped", e);
    } finally {
        JOrphanUtils.closeQuietly(mainSocket);
    }
}