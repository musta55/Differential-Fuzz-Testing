/**
 * Constructor.
 *
 * @param name         the name/long option
 * @param flags        the flags
 * @param id           the id/character option
 * @param description  description of option usage
 * @param incompatible descriptors for incompatible options
 */
public CLOptionDescriptor(final String name, final int flags, final int id, final String description, final CLOptionDescriptor[] incompatible) {
    checkFlags(flags);
    this.id = id;
    this.name = name;
    this.flags = flags;
    this.description = description;
    this.incompatible = extractIncompatibleIds(incompatible);
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

