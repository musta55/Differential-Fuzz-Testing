/**
 * Listen on the daemon port and handle incoming requests. This method will
 * not exit until {@link #stopServer()} is called or an error occurs.
 */
@Override
public void run() {
    running = true;
    log.info("Test Script Recorder up and running!");
    Map<String, String> pageEncodings = Collections.synchronizedMap(new HashMap<>());
    pageEncodings.put(AbstractSamplerCreator.DEFAULT_ENCODING_KEY, target.getDefaultEncoding());
    try {
        while (running) {
            try {
                Socket clientSocket = mainSocket.accept();
                if (running) {
                    Proxy thd = createAndConfigureProxy(clientSocket, pageEncodings);
                    thd.start();
                }
            } catch (InterruptedIOException e) {
                // Timeout occurred. Ignore, and keep looping until we're
                // told to stop running.
                log.debug("Accept timeout occurred", e);
            }
        }
        log.info("HTTP(S) Test Script Recorder stopped");
    } catch (Exception e) {
        log.warn("HTTP(S) Test Script Recorder stopped", e);
    } finally {
        JOrphanUtils.closeQuietly(mainSocket);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Proxy createAndConfigureProxy(Socket clientSocket, Map<String, String> pageEncodings) throws Exception {
    Proxy thd = proxyClass.getDeclaredConstructor().newInstance();
    thd.configure(clientSocket, target, pageEncodings, Collections.emptyMap());
    return thd;
}

