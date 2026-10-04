public static Date parseDateBySeparator(String dateString) {
    return parseDate(dateFormat__ddMMyyyyBySeparator, dateString);
}
// ---- helper method(s) introduced by the refactoring ----
private static Date parseDate(FastDateFormat sdf, String dateString) {
    if (dateString == null || dateString.isEmpty() || "null".equals(dateString)) {
        return null;
    }
    try {
        return sdf.parse(dateString);
    } catch (ParseException e) {
        log.error("parseDate", e);
        return null;
    }
}

