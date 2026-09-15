protected void authenticateMessage(byte[] buffer, int offset, int size) {
    if (token != null) {
        boolean authenticated = false;
        if (size == token.length) {
            int match = 0;
            while ((match < token.length) && (buffer[offset + match] == token[match])) {
                ++match;
            }
            if (match == token.length) {
                authenticated = true;
            }
        }
        if (!authenticated) {
            throw new AccessControlException("Buffer server security is enabled." + " Access is restricted without proper credentials.");
        }
    }
}