/**
 * @deprecated For use by test code only (serialisation tests)
 */
@Deprecated
public ReadException() {
    this(null, null);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Constructor
 * @param message Message
 * @param cause Source cause
 */
public ReadException(String message, Throwable cause) {
    this(message, cause, null);
}

