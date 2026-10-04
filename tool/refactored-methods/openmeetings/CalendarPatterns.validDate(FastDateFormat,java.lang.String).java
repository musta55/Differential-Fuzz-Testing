private static Date validDate(FastDateFormat sdf, String testdate) {
    try {
        Date resultDate = sdf.parse(testdate);
        return sdf.format(resultDate).equals(testdate) ? resultDate : null;
    } catch (ParseException e) {
        return null;
    }
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

