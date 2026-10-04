/**
 * {@inheritDoc}
 */
@Override
public String[] getDefaultCipherSuites() {
    return getSupportedCipherSuites();
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleException(Exception e) {
    throw new RuntimeException("Could not create the SSL context", e);
}

