@Override
public LocalDate unmarshal(String v) throws Exception {
    if (v == null || "null".equals(v)) {
        return null;
    }
    try {
        return parseDate(v);
    } catch (DateTimeParseException e) {
        // no-op
    }
    try {
        return parseEpochMillis(v);
    } catch (NumberFormatException e) {
        // no-op
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private LocalDate parseDate(String v) {
    return LocalDate.parse(v, DATE_FORMATTER);
}

private LocalDate parseEpochMillis(String v) {
    long epochMillis = Long.parseLong(v);
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneOffset.UTC).toLocalDate();
}

