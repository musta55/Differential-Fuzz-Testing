/**
 * Validates the logger pattern and the level.
 * @param pattern
 * @param level
 * @return
 */
public static boolean validateLoggersLevel(String pattern, String level) {
    return isValidLoggerPattern(pattern) && isValidLogLevel(level);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Validates the logger pattern.
 * @param pattern
 * @return
 */
private static boolean isValidLoggerPattern(String pattern) {
    return LOGGERS_PATTERN.matcher(pattern).matches();
}

/**
 * Validates the log level.
 * @param level
 * @return
 */
private static boolean isValidLogLevel(String level) {
    return Level.toLevel(level, null) != null;
}

