@Override
public String marshal(LocalDate v) throws Exception {
    return v.format(DATE_FORMATTER);
}
// ---- helper method(s) introduced by the refactoring ----
private LocalDate parseDate(String v) {
    return LocalDate.parse(v, DATE_FORMATTER);
}

private LocalDate parseEpochMillis(String v) {
    long epochMillis = Long.parseLong(v);
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneOffset.UTC).toLocalDate();
}

