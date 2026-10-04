public static Date parseDateWithHour(String dateString) {
    return parseDate(dateFormat__ddMMyyyyHHmmss, dateString);
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

