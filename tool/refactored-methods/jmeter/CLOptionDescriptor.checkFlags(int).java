private static void checkFlags(final int flags) {
    int modeCount = 0;
    if (isArgumentRequired(flags)) {
        modeCount++;
    }
    if (isArgumentOptional(flags)) {
        modeCount++;
    }
    if (isArgumentDisallowed(flags)) {
        modeCount++;
    }
    if (areArgumentsRequired2(flags)) {
        modeCount++;
    }
    if (modeCount == 0) {
        throw new IllegalStateException("No mode specified for option");
    } else if (modeCount != 1) {
        throw new IllegalStateException("Multiple modes specified for option");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isArgumentRequired(final int flags) {
    return (ARGUMENT_REQUIRED & flags) == ARGUMENT_REQUIRED;
}

private static boolean isArgumentOptional(final int flags) {
    return (ARGUMENT_OPTIONAL & flags) == ARGUMENT_OPTIONAL;
}

private static boolean isArgumentDisallowed(final int flags) {
    return (ARGUMENT_DISALLOWED & flags) == ARGUMENT_DISALLOWED;
}

private static boolean areArgumentsRequired2(final int flags) {
    return (ARGUMENTS_REQUIRED_2 & flags) == ARGUMENTS_REQUIRED_2;
}

private static boolean isDuplicatesAllowed(final int flags) {
    return (DUPLICATES_ALLOWED & flags) != 0;
}

private static int[] extractIncompatibleIds(final CLOptionDescriptor[] incompatible) {
    final int[] ids = new int[incompatible.length];
    for (int i = 0; i < incompatible.length; i++) {
        ids[i] = incompatible[i].getId();
    }
    return ids;
}

