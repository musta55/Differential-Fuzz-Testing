/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    DateTimeFormatter formatter;
    if (values.length > 3) {
        String localeAsString = values[3].execute().trim();
        if (!localeAsString.trim().isEmpty()) {
            locale = LocaleUtils.toLocale(localeAsString);
        }
    }
    String format = values[0].execute().trim();
    if (!StringUtils.isEmpty(format)) {
        try {
            LocaleFormatObject lfo = new LocaleFormatObject(format, locale);
            formatter = dateRandomFormatterCache.get(lfo, key -> createFormatter((LocaleFormatObject) key));
        } catch (IllegalArgumentException ex) {
            log.error("Format date pattern '{}' is invalid (see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)", format, // $NON-NLS-1$
            ex);
            return "";
        }
    } else {
        try {
            LocaleFormatObject lfo = new LocaleFormatObject("yyyy-MM-dd", locale);
            formatter = dateRandomFormatterCache.get(lfo, key -> createFormatter((LocaleFormatObject) key));
        } catch (IllegalArgumentException ex) {
            log.error("Format date pattern '{}' is invalid (see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)", format, // $NON-NLS-1$
            ex);
            return "";
        }
    }
    String dateStart = values[1].execute().trim();
    long localStartDate = 0;
    if (!dateStart.isEmpty()) {
        try {
            localStartDate = LocalDate.parse(dateStart, formatter).toEpochDay();
        } catch (DateTimeParseException | NumberFormatException ex) {
            // $NON-NLS-1$
            log.error("Failed to parse Start Date '{}'", dateStart, ex);
        }
    } else {
        try {
            localStartDate = LocalDate.now(systemDefaultZoneID).toEpochDay();
        } catch (DateTimeParseException | NumberFormatException ex) {
            // $NON-NLS-1$
            log.error("Failed to create current date '{}'", dateStart, ex);
        }
    }
    long localEndDate = 0;
    String dateEnd = values[2].execute().trim();
    try {
        localEndDate = LocalDate.parse(dateEnd, formatter).toEpochDay();
    } catch (DateTimeParseException | NumberFormatException ex) {
        // $NON-NLS-1$
        log.error("Failed to parse End date '{}'", dateEnd, ex);
    }
    // Generate the random date
    String dateString = "";
    if (localEndDate < localStartDate) {
        // $NON-NLS-1$
        log.error("End Date '{}' must be greater than Start Date '{}'", dateEnd, dateStart);
    } else {
        long randomDay = ThreadLocalRandom.current().nextLong(localStartDate, localEndDate);
        try {
            dateString = LocalDate.ofEpochDay(randomDay).format(formatter);
        } catch (DateTimeParseException | NumberFormatException ex) {
            // $NON-NLS-1$
            log.error("Failed to generate random date '{}'", randomDay, ex);
        }
        addVariableValue(dateString, values, 4);
    }
    return dateString;
}