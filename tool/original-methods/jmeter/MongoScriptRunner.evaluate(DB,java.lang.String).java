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
    if (log.isDebugEnabled()) {
        log.debug("database: " + db.getName() + ", script: " + script);
    }
    db.requestStart();
    try {
        db.requestEnsureConnection();
        Object result = db.eval(script);
        if (log.isDebugEnabled()) {
            log.debug("Result : " + result);
        }
        return result;
    } finally {
        db.requestDone();
    }
}