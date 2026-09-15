protected void processCallback(Callback callback) throws IOException, UnsupportedCallbackException {
    if (callback instanceof NameCallback) {
        handleNameCallback((NameCallback) callback);
    } else if (callback instanceof PasswordCallback) {
        handlePasswordCallback((PasswordCallback) callback);
    } else if (callback instanceof RealmCallback) {
        handleRealmCallback((RealmCallback) callback);
    } else if (callback instanceof TextOutputCallback) {
        handleTextOutputCallback((TextOutputCallback) callback);
    } else {
        throw new UnsupportedCallbackException(callback);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleNameCallback(NameCallback namecb) {
    namecb.setName(context.getValue(SecurityContext.USER_NAME));
}

private void handlePasswordCallback(PasswordCallback passcb) {
    passcb.setPassword(context.getValue(SecurityContext.PASSWORD));
}

private void handleRealmCallback(RealmCallback realmcb) {
    realmcb.setText(context.getValue(SecurityContext.REALM));
}

private void handleTextOutputCallback(TextOutputCallback textcb) {
    switch(textcb.getMessageType()) {
        case TextOutputCallback.INFORMATION:
            logger.info(textcb.getMessage());
            break;
        case TextOutputCallback.WARNING:
            logger.warn(textcb.getMessage());
            break;
        case TextOutputCallback.ERROR:
            logger.error(textcb.getMessage());
            break;
        default:
            logger.debug("Auth message type {}, message {}", textcb.getMessageType(), textcb.getMessage());
            break;
    }
}

