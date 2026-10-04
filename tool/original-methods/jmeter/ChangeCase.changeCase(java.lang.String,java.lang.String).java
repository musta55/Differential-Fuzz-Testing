protected String changeCase(String originalString, String mode) {
    String targetString = originalString;
    // mode is case insensitive, allow upper for example
    ChangeCaseMode changeCaseMode = ChangeCaseMode.typeOf(mode.toUpperCase(Locale.ROOT));
    if (changeCaseMode != null) {
        switch(changeCaseMode) {
            case UPPER:
                targetString = StringUtils.upperCase(originalString);
                break;
            case LOWER:
                targetString = StringUtils.lowerCase(originalString);
                break;
            case CAPITALIZE:
                targetString = StringUtils.capitalize(originalString);
                break;
        }
    } else {
        LOGGER.error("Unknown mode {}, returning {} unchanged", mode, targetString);
    }
    return targetString;
}