protected String changeCase(String originalString, String mode) {
    ChangeCaseMode changeCaseMode = ChangeCaseMode.typeOf(mode.toUpperCase(Locale.ROOT));
    if (changeCaseMode != null) {
        Function<String, String> function = MODE_FUNCTIONS.get(changeCaseMode);
        if (function != null) {
            return function.apply(originalString);
        }
    }
    LOGGER.error("Unknown mode {}, returning {} unchanged", mode, originalString);
    return originalString;
}
// ---- helper method(s) introduced by the refactoring ----
@Override
public String apply(String input) {
    // Default behavior for Function interface
    return StringUtils.upperCase(input);
}

