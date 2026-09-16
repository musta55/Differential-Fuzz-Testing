/**
 * sends a mail address to the user with his account data
 *
 * @param username - login of the registered user
 * @param email - email of the registered user
 * @param hash - activation hash
 * @param sendEmailWithVerificationCode - if email with verification code should be sent
 * @param langId - language Id
 */
public void sendMail(String username, String email, String hash, boolean sendEmailWithVerificationCode, Long langId) {
    log.debug("sendMail:: username = {}, email = {}", username, email);
    ensureApplication(getLanguageId(langId));
    String activationLink = getActivationLink(hash);
    if (shouldSendRegisterEmail()) {
        sendRegistrationEmail(email, username, activationLink, sendEmailWithVerificationCode);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Long getLanguageId(Long langId) {
    return langId != null ? langId : getDefaultLang();
}

private String getActivationLink(String hash) {
    return getApp().urlForActivatePage(new PageParameters().add("u", hash));
}

private boolean shouldSendRegisterEmail() {
    return isSendRegisterEmail();
}

private void sendRegistrationEmail(String email, String username, String activationLink, boolean sendEmailWithVerificationCode) {
    mailHandler.send(email, getString("512"), RegisterUserTemplate.getEmail(username, email, sendEmailWithVerificationCode ? activationLink : null));
}

