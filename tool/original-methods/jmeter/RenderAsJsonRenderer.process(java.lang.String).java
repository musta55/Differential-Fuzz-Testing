@Override
protected String process(String textToParse) {
    String expression = getExpression();
    try {
        List<Object> matchStrings = extractWithTechnology(textToParse, expression);
        if (matchStrings.isEmpty()) {
            //$NON-NLS-1$
            return NO_MATCH;
        } else {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (Object obj : matchStrings) {
                String objAsString = //$NON-NLS-1$
                obj != null ? obj.toString() : "";
                //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
                builder.append("Result[").append(i++).append("]=").append(objAsString).append("\n");
            }
            return builder.toString();
        }
    } catch (Exception e) {
        // NOSONAR We handle it through return message
        log.debug("Exception extracting from '{}' with expression '{}'", textToParse, expression);
        //$NON-NLS-1$
        return "Exception: " + e.getMessage();
    }
}