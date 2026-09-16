/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    DateTimeFormatter formatter = getFormatter();
    long localStartDate = getStartDate(formatter);
    long localEndDate = getEndDate(formatter);
    // Generate the random date
    String dateString = "";
    if (localEndDate < localStartDate) {
        // $NON-NLS-1$
        log.error("End Date '{}' must be greater than Start Date '{}'", values[2].execute().trim(), values[1].execute().trim());
    } else {
        long randomDay = ThreadLocalRandom.current().nextLong(localStartDate, localEndDate);
        dateString = formatRandomDate(randomDay, formatter);
        addVariableValue(dateString, values, 4);
    }
    return dateString;
}
// ---- helper method(s) introduced by the refactoring ----
private DateTimeFormatter getFormatter() {
    String format = values[0].execute().trim();
    if (StringUtils.isEmpty(format)) {
        format = "yyyy-MM-dd";
    }
    LocaleFormatObject lfo = new LocaleFormatObject(format, locale);
    try {
        return dateRandomFormatterCache.get(lfo, key -> createFormatter(key));
    } catch (IllegalArgumentException ex) {
        // $NON-NLS-1$
        log.error("Format date pattern '{}' is invalid (see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)", format, ex);
        return null;
    }
}

private long getStartDate(DateTimeFormatter formatter) {
    String dateStart = values[1].execute().trim();
    if (!dateStart.isEmpty()) {
        try {
            return LocalDate.parse(dateStart, formatter).toEpochDay();
        } catch (DateTimeParseException ex) {
            // $NON-NLS-1$
            log.error("Failed to parse Start Date '{}'", dateStart, ex);
        }
    }
    return LocalDate.now(systemDefaultZoneID).toEpochDay();
}

private long getEndDate(DateTimeFormatter formatter) {
    String dateEnd = values[2].execute().trim();
    try {
        return LocalDate.parse(dateEnd, formatter).toEpochDay();
    } catch (DateTimeParseException ex) {
        // $NON-NLS-1$
        log.error("Failed to parse End date '{}'", dateEnd, ex);
    }
    return 0;
}

private static String formatRandomDate(long randomDay, DateTimeFormatter formatter) {
    try {
        return LocalDate.ofEpochDay(randomDay).format(formatter);
    } catch (DateTimeParseException ex) {
        // $NON-NLS-1$
        log.error("Failed to generate random date '{}'", randomDay, ex);
        return "";
    }
}

