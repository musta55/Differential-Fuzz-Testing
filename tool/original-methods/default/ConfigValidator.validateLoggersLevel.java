/**
 * Validates the logger pattern and the level.
 * @param pattern
 * @param level
 * @return
 */
public static boolean validateLoggersLevel(String pattern, String level) {
    if (!LOGGERS_PATTERN.matcher(pattern).matches()) {
        return false;
    }
    if (Level.toLevel(level, null) == null) {
        return false;
    }
    return true;
}