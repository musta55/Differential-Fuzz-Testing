/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    try {
        ScriptEngine scriptEngine = getScriptEngine();
        Object result = processFileOrScript(scriptEngine, null);
        return parseDelay(result);
    } catch (Exception e) {
        handleException(e);
        return 0;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static long parseDelay(Object result) {
    if (result == null) {
        log.warn("Script did not return a value");
        return 0;
    }
    try {
        return Long.parseLong(result.toString());
    } catch (NumberFormatException e) {
        log.error("Failed to parse delay from script result: {}", result, e);
        return 0;
    }
}

private void handleException(Exception e) {
    log.error("Problem in JSR223 script, {}", getName(), e);
}

