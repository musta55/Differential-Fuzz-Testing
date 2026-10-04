public ErrorMessagePanel(String id, String msg, Throwable err) {
    super(id);
    logError(msg, err);
    addMessageLabel(msg);
    addErrorLabel(err);
}
// ---- helper method(s) introduced by the refactoring ----
private void logError(String msg, Throwable err) {
    log.error(msg, err);
}

private void addMessageLabel(String msg) {
    add(new Label("msg", msg));
}

private void addErrorLabel(Throwable err) {
    StringBuilderWriter sw = new StringBuilderWriter();
    err.printStackTrace(new PrintWriter(sw));
    add(new Label("err", sw.toString()));
}

