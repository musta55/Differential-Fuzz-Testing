private static ProcessResult exec(String process, List<String> argv, Map<String, String> env, boolean optional) {
    ProcessResult res = new ProcessResult().setProcess(process).setOptional(optional);
    debugCommandStart(process, argv);
    Process proc = null;
    StreamWatcher errorWatcher = null;
    StreamWatcher inputWatcher = null;
    long start = System.currentTimeMillis();
    try {
        res.setCommand(getCommand(argv)).setOut("");
        ProcessBuilder pb = new ProcessBuilder(argv);
        pb.environment().putAll(env);
        proc = pb.start();
        errorWatcher = new StreamWatcher(proc.getErrorStream());
        inputWatcher = new StreamWatcher(proc.getInputStream());
        startWatchers(errorWatcher, inputWatcher);
        proc.waitFor(getExtProcessTtl(), TimeUnit.MINUTES);
        res.setExitCode(proc.exitValue()).setOut(inputWatcher.toString()).setError(errorWatcher.toString());
    } catch (InterruptedException e) {
        handleInterruptedException(e, start, res);
        Thread.currentThread().interrupt();
    } catch (Throwable t) {
        handleException(t, start, res);
    } finally {
        cleanupWatchers(errorWatcher, inputWatcher);
        destroyProcess(proc);
    }
    debugCommandEnd(process);
    return res;
}
// ---- helper method(s) introduced by the refactoring ----
private static void startWatchers(StreamWatcher errorWatcher, StreamWatcher inputWatcher) {
    errorWatcher.start();
    inputWatcher.start();
}

private static void handleInterruptedException(InterruptedException e, long start, ProcessResult res) {
    log.error("executeScript", e);
    res.setExitCode(-1).setError("Exception after " + formatMillis(System.currentTimeMillis() - start) + " of work; " + e.getMessage()).setException(e.toString());
}

private static void handleException(Throwable t, long start, ProcessResult res) {
    log.error("executeScript", t);
    res.setExitCode(-1).setError("Exception after " + formatMillis(System.currentTimeMillis() - start) + " of work; " + t.getMessage()).setException(t.toString());
}

private static void cleanupWatchers(StreamWatcher errorWatcher, StreamWatcher inputWatcher) {
    if (errorWatcher != null) {
        errorWatcher.finish();
    }
    if (inputWatcher != null) {
        inputWatcher.finish();
    }
}

private static void destroyProcess(Process proc) {
    if (proc != null) {
        proc.destroy();
    }
}

