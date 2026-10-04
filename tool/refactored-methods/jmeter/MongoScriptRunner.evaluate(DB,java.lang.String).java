/**
 * Evaluate a script on the database
 *
 * @param db
 *            database connection to use
 * @param script
 *            script to evaluate on the database
 * @return result of evaluation on the database
 * @throws Exception
 *             when evaluation on the database fails
 */
@SuppressWarnings("deprecation")
public Object evaluate(DB db, String script) throws Exception {
    logDebugInformation(db, script);
    db.requestStart();
    try {
        db.requestEnsureConnection();
        Object result = db.eval(script);
        logDebugResult(result);
        return result;
    } finally {
        db.requestDone();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void logDebugInformation(DB db, String script) {
    if (log.isDebugEnabled()) {
        log.debug("database: " + db.getName() + ", script: " + script);
    }
}

private static void logDebugResult(Object result) {
    if (log.isDebugEnabled()) {
        log.debug("Result : " + result);
    }
}

