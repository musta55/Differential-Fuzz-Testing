public Subject getSubjectForUser(final String username, final String password) {
    FutureTask<Subject> task = new FutureTask<>(() -> {
        LoginContext loginCtx;
        try {
            loginCtx = new LoginContext(JAAS_APPLICATION, new LoginCallbackHandler(username, password));
            loginCtx.login();
            return loginCtx.getSubject();
        } catch (LoginException e) {
            log.warn("Could not log in user {}", username, e);
        }
        return null;
    });
    if (log.isDebugEnabled()) {
        log.debug("Subject cached:{} before:{}", subjects.keySet(), username);
    }
    Future<Subject> subjectFuture = subjects.putIfAbsent(username, task);
    if (subjectFuture == null) {
        subjectFuture = task;
        // NOSONAR we just execute method
        task.run();
    }
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