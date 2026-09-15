protected void processCallback(Callback callback) throws IOException, UnsupportedCallbackException {
    if (callback instanceof NameCallback) {
        NameCallback namecb = (NameCallback) callback;
        namecb.setName(context.getValue(SecurityContext.USER_NAME));
    } else if (callback instanceof PasswordCallback) {
        PasswordCallback passcb = (PasswordCallback) callback;
        passcb.setPassword(context.getValue(SecurityContext.PASSWORD));
    } else if (callback instanceof RealmCallback) {
        RealmCallback realmcb = (RealmCallback) callback;
        realmcb.setText(context.getValue(SecurityContext.REALM));
    } else if (callback instanceof TextOutputCallback) {
        TextOutputCallback textcb = (TextOutputCallback) callback;
        if (textcb.getMessageType() == TextOutputCallback.INFORMATION) {
            logger.info(textcb.getMessage());
        } else if (textcb.getMessageType() == TextOutputCallback.WARNING) {
            logger.warn(textcb.getMessage());
        } else if (textcb.getMessageType() == TextOutputCallback.ERROR) {
            logger.error(textcb.getMessage());
        } else {
            logger.debug("Auth message type {}, message {}", textcb.getMessageType(), textcb.getMessage());
        }
    } else {
        throw new UnsupportedCallbackException(callback);
    }
}