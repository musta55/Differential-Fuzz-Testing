public Subject getSubjectForUser(final String username, final String password) {
    Future<Subject> subjectFuture = createAndCacheSubjectFuture(username, password);
    try {
        return subjectFuture.get();
    } catch (InterruptedException e1) {
        log.warn("Interrupted while getting subject for {}", username, e1);
        Thread.currentThread().interrupt();
    } catch (ExecutionException e1) {
        log.warn("Execution of getting subject for {} failed", username, e1);
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private Future<Subject> createAndCacheSubjectFuture(String username, String password) {
    FutureTask<Subject> task = new FutureTask<>(() -> getSubject(username, password));
    if (log.isDebugEnabled()) {
        log.debug("Subject cached:{} before:{}", subjects.keySet(), username);
    }
    Future<Subject> subjectFuture = subjects.putIfAbsent(username, task);
    if (subjectFuture == null) {
        subjectFuture = task;
        // NOSONAR we just execute method
        task.run();
    }
    return subjectFuture;
}

private static Subject getSubject(String username, String password) {
    try {
        LoginContext loginCtx = new LoginContext(JAAS_APPLICATION, new LoginCallbackHandler(username, password));
        loginCtx.login();
        return loginCtx.getSubject();
    } catch (LoginException e) {
        log.warn("Could not log in user {}", username, e);
    }
    return null;
}

