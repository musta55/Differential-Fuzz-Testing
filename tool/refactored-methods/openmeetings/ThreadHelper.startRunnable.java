public static void startRunnable(Runnable r, String name) {
    Thread t = createThread(r);
    if (!Strings.isEmpty(name)) {
        t.setName(name);
    }
    t.start();
}
// ---- helper method(s) introduced by the refactoring ----
private static Thread createThread(Runnable r) {
    return new Thread(() -> {
        setThreadContext();
        try {
            r.run();
        } finally {
            detachThreadContext();
        }
    });
}

private static void setThreadContext() {
    Application app = Application.get();
    WebSession session = WebSession.get();
    RequestCycle rc = RequestCycle.get();
    ThreadContext.setApplication(app);
    ThreadContext.setSession(session);
    ThreadContext.setRequestCycle(rc);
}

private static void detachThreadContext() {
    ThreadContext.detach();
}

