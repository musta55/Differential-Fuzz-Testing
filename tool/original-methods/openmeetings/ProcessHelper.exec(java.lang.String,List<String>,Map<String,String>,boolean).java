private static ProcessResult exec(String process, List<String> argv, Map<String, String> env, boolean optional) {
    ProcessResult res = new ProcessResult().setProcess(process).setOptional(optional);
    debugCommandStart(process, argv);
    Process proc = null;
    StreamWatcher errorWatcher = null;
    StreamWatcher inputWatcher = null;
    final long start = System.currentTimeMillis();
    try {
        res.setCommand(getCommand(argv)).setOut("");
        // By using the process Builder we have access to modify the
        // environment variables
        // that is handy to set variables to run it inside eclipse
        ProcessBuilder pb = new ProcessBuilder(argv);
        pb.environment().putAll(env);
        proc = pb.start();
        errorWatcher = new StreamWatcher(proc.getErrorStream());
        inputWatcher = new StreamWatcher(proc.getInputStream());
        errorWatcher.start();
        inputWatcher.start();
        // 20-minute timeout for command execution
        // FFMPEG conversion of Recordings may take a real long time until
        // its finished
        proc.waitFor(getExtProcessTtl(), TimeUnit.MINUTES);
        res.setExitCode(proc.exitValue()).setOut(inputWatcher.toString()).setError(errorWatcher.toString());
    } catch (InterruptedException e) {
        onException(e, start, res);
        Thread.currentThread().interrupt();
    } catch (Throwable t) {
        onException(t, start, res);
    } finally {
        if (errorWatcher != null) {
            errorWatcher.finish();
        }
        if (inputWatcher != null) {
            inputWatcher.finish();
        }
        if (proc != null) {
            proc.destroy();
        }
    }
    debugCommandEnd(process);
    return res;
}