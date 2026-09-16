public static Date get(String val) {
    if (Strings.isEmpty(val)) {
        return null;
    }
    for (FastDateFormat df : formats) {
        Date parsedDate = attemptParse(df, val);
        if (parsedDate != null) {
            return parsedDate;
        }
    }
    throw new IllegalArgumentException("Unparsable format: " + val);
}
// ---- helper method(s) introduced by the refactoring ----
private static Date attemptParse(FastDateFormat df, String val) {
    try {
        return df.parse(val);
    } catch (ParseException e) {
        return null;
    }
}

