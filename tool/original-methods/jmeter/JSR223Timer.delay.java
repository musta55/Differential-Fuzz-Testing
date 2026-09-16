/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    long delay = 0;
    try {
        ScriptEngine scriptEngine = getScriptEngine();
        Object o = processFileOrScript(scriptEngine, null);
        if (o == null) {
            log.warn("Script did not return a value");
            return 0;
        }
        delay = Long.parseLong(o.toString());
    } catch (NumberFormatException | IOException | ScriptException e) {
        log.error("Problem in JSR223 script, {}", getName(), e);
    }
    return delay;
}