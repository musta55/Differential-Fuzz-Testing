private String filterString(final String content) {
    if (stringsToSkip == null || stringsToSkip.isEmpty()) {
        return content;
    }
    if (USE_JAVA_REGEX) {
        String result = content;
        for (SubstitutionElement element : stringsToSkip) {
            result = result.replaceAll(element.getRegex(), element.getSubstitute());
        }
        return result;
    } else {
        String result = content;
        for (SubstitutionElement regex : stringsToSkip) {
            emptySub.setSubstitution(regex.getSubstitute());
            result = Util.substitute(JMeterUtils.getMatcher(), JMeterUtils.getPatternCache().getPattern(regex.getRegex()), emptySub, result, Util.SUBSTITUTE_ALL);
        }
        return result;
    }
}