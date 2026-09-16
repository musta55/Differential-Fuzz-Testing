public FileItemLog add(String name, BaseFileItem f, ProcessResult r) {
    logTrace(name, f, r);
    FileItemLog logEntry = createFileItemLog(name, f, r);
    persistLogEntry(logEntry);
    return logEntry;
}
// ---- helper method(s) introduced by the refactoring ----
private void logTrace(String name, BaseFileItem f, ProcessResult r) {
    log.trace("Adding log: {}, {}, {}", name, f, r);
}

private FileItemLog createFileItemLog(String name, BaseFileItem f, ProcessResult r) {
    return new FileItemLog().setInserted(new Date()).setExitCode(r.getExitCode()).setFileId(f.getId()).setMessage(r.buildLogMessage()).setName(name).setOptional(r.isOptional());
}

private void persistLogEntry(FileItemLog logEntry) {
    em.persist(logEntry);
}

