/**
 * If the condition fails, print the Op and its Id, along with the message formatted with objects.
 * @param condition Condition to test
 * @param hop Hop to print as a cause of the problem, if the condition fails
 * @param message Message to print if the condition fails
 * @param objects Objects to print with the message, as per String.format
 */
public static void check(boolean condition, Hop hop, String message, Object... objects) {
    if (!condition)
        throw new HopsException(String.format(hop.getOpString() + " id=" + hop.getHopID() + " " + message, objects));
}