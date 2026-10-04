public static Date parseImportDate(String dateString) {
    if (dateString == null) {
        log.error("parseImportDate:: Could not parse date string {}", dateString);
        return null;
    }
    Date resultDate = validDate(dateFormat__ddMMyyyyHHmmss, dateString);
    if (resultDate != null) {
        return resultDate;
    }
    resultDate = validDate(dateFormat__ddMMyyyy, dateString);
    if (resultDate != null) {
        return resultDate;
    }
    resultDate = validDate(dateString);
    if (resultDate != null) {
        return resultDate;
    }
    log.error("parseImportDate:: Could not parse date string {}", dateString);
    return null;
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

