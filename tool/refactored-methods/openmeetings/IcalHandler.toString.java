/**
 * Retrieving Data as String
 */
@Override
public String toString() {
    if (icsCalendar == null) {
        return "";
    }
    try {
        return convertToString(toByteArray());
    } catch (IOException e) {
        logError(e);
    }
    return "";
}
// ---- helper method(s) introduced by the refactoring ----
private String convertToString(byte[] byteArray) {
    return new String(byteArray, UTF_8);
}

private void logError(Exception e) {
    log.error("Unexpected error", e);
}

