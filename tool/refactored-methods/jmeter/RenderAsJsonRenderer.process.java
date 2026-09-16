@Override
protected String process(String textToParse) {
    String expression = getExpression();
    try {
        List<Object> matchStrings = extractWithTechnology(textToParse, expression);
        return formatMatchStrings(matchStrings);
    } catch (Exception e) {
        // NOSONAR We handle it through return message
        log.debug("Exception extracting from '{}' with expression '{}'", textToParse, expression);
        //$NON-NLS-1$
        return "Exception: " + e.getMessage();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatMatchStrings(List<Object> matchStrings) {
    if (matchStrings.isEmpty()) {
        //$NON-NLS-1$
        return NO_MATCH;
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < matchStrings.size(); i++) {
        //$NON-NLS-1$
        String objAsString = matchStrings.get(i) != null ? matchStrings.get(i).toString() : "";
        //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
        builder.append("Result[").append(i).append("]=").append(objAsString).append("\n");
    }
    return builder.toString();
}

