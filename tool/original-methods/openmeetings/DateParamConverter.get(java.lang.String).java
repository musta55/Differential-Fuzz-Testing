public static Date get(String val) {
    if (Strings.isEmpty(val)) {
        return null;
    }
    for (FastDateFormat df : formats) {
        try {
            return df.parse(val);
        } catch (ParseException e) {
            // no-op
        }
    }
    throw new IllegalArgumentException("Unparsable format: " + val);
}