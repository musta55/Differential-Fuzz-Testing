@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String dateString = values[0].execute();
    String sourceDateFormat = values[1].execute();
    String targetDateFormat = values[2].execute();
    try {
        String newDate = formatDate(dateString, sourceDateFormat, targetDateFormat);
        addVariableValue(newDate, values, 3);
        return newDate;
    } catch (Exception e) {
        log.error("Error calling {} function with value {}, source format {}, target format {}, ", KEY, dateString, sourceDateFormat, targetDateFormat, e);
    }
    return "";
    ////$NON-NLS-1$
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatDate(String dateString, String sourceDateFormat, String targetDateFormat) {
    DateTimeFormatter targetDateFormatter = DateTimeFormatter.ofPattern(targetDateFormat).withZone(ZoneId.systemDefault());
    if (sourceDateFormat != null && !sourceDateFormat.isEmpty()) {
        DateTimeFormatter sourceDateFormatter = DateTimeFormatter.ofPattern(sourceDateFormat).withZone(ZoneId.systemDefault());
        return targetDateFormatter.format(sourceDateFormatter.parse(dateString));
    } else {
        // dateString will be an epoch time
        return targetDateFormatter.format(Instant.ofEpochMilli(Long.parseLong(dateString)));
    }
}

