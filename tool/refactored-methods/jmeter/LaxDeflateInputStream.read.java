@Override
public int read(byte[] b) throws IOException {
    try {
        return super.read(b);
    } catch (final EOFException ex) {
        return handleEOFExceptionForRelaxMode(ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Handles EOFException based on the relax mode.
 *
 * @param ex EOFException
 * @return -1 if relax mode is enabled, otherwise rethrows the exception
 * @throws EOFException if relax mode is disabled
 */
private int handleEOFExceptionForRelaxMode(final EOFException ex) throws EOFException {
    if (relax) {
        return -1;
    } else {
        throw ex;
    }
}

