@Override
public String marshal(LocalDate v) throws Exception {
    return v.format(DateTimeFormatter.ofPattern(CalendarPatterns.ISO8601_DATE_FORMAT_STRING));
}