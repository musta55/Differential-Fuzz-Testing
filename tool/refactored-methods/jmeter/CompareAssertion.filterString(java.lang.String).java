private String filterString(final String content) {
    if (stringsToSkip == null || stringsToSkip.isEmpty()) {
        return content;
    }
    String result = content;
    if (USE_JAVA_REGEX) {
        for (SubstitutionElement element : stringsToSkip) {
            result = result.replaceAll(element.getRegex(), element.getSubstitute());
        }
    } else {
        for (SubstitutionElement regex : stringsToSkip) {
            emptySub.setSubstitution(regex.getSubstitute());
            result = Util.substitute(JMeterUtils.getMatcher(), JMeterUtils.getPatternCache().getPattern(regex.getRegex()), emptySub, result, Util.SUBSTITUTE_ALL);
        }
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendIfNotNull(StringBuilder buf, String str) {
    if (str != null) {
        buf.append(str.trim());
    }
}

