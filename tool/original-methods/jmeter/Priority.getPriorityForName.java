/**
 * Retrieve a Priority object for the name parameter.
 *
 * @param priority the priority name
 * @return the Priority for name
 */
public static Priority getPriorityForName(final String priority) {
    if (Priority.DEBUG.getName().equals(priority)) {
        return Priority.DEBUG;
    } else if (Priority.INFO.getName().equals(priority)) {
        return Priority.INFO;
    } else if (Priority.WARN.getName().equals(priority)) {
        return Priority.WARN;
    } else if (Priority.ERROR.getName().equals(priority)) {
        return Priority.ERROR;
    } else if (Priority.FATAL_ERROR.getName().equals(priority)) {
        return Priority.FATAL_ERROR;
    } else if (Priority.NONE.getName().equals(priority)) {
        return Priority.NONE;
    } else {
        return Priority.DEBUG;
    }
}