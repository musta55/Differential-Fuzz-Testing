protected void authenticateMessage(byte[] buffer, int offset, int size) {
    if (token != null && isTokenMatch(buffer, offset, size)) {
        return;
    }
    throw new AccessControlException("Buffer server security is enabled." + " Access is restricted without proper credentials.");
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isTokenMatch(byte[] buffer, int offset, int size) {
    return size == token.length && matches(buffer, offset);
}

private boolean matches(byte[] buffer, int offset) {
    for (int i = 0; i < token.length; i++) {
        if (buffer[offset + i] != token[i]) {
            return false;
        }
    }
    return true;
}

