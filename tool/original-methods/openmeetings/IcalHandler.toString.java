/**
 * Retrieving Data as String
 */
@Override
public String toString() {
    if (icsCalendar == null) {
        return "";
    }
    try {
        return new String(toByteArray(), UTF_8);
    } catch (IOException e) {
        log.error("Unexpected error", e);
    }
    return "";
}