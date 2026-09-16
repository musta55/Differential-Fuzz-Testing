/**
 * Retrieve a Priority object for the name parameter.
 *
 * @param priority the priority name
 * @return the Priority for name
 */
public static Priority getPriorityForName(final String priority) {
    return PRIORITY_MAP.getOrDefault(priority, DEBUG);
}