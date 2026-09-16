@Override
public LocalDate unmarshal(String v) throws Exception {
    if (v == null || "null".equals(v)) {
        return null;
    }
    try {
        return LocalDate.parse(v, DateTimeFormatter.ofPattern(CalendarPatterns.ISO8601_DATE_FORMAT_STRING));
    } catch (Exception err) {
        //no-op
    }
    try {
        Long t = Long.valueOf(v);
        if (t != null) {
            return Instant.ofEpochMilli(t).atZone(ZoneOffset.UTC).toLocalDate();
        }
    } catch (Exception err) {
        //no-op
    }
    return null;
}