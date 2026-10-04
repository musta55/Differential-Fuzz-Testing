public static void startRunnable(Runnable r, String name) {
    final Application app = Application.get();
    final WebSession session = WebSession.get();
    final RequestCycle rc = RequestCycle.get();
    Thread t = new Thread(() -> {
        try {
            ThreadContext.setApplication(app);
            ThreadContext.setSession(session);
            ThreadContext.setRequestCycle(rc);
            r.run();
        } finally {
            ThreadContext.detach();
        }
    });
    if (!Strings.isEmpty(name)) {
        t.setName(name);
    }
    t.start();
}