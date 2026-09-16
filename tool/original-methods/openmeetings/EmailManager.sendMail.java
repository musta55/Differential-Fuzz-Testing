/**
 * sends a mail address to the user with his account data
 *
 * @param username - login of the registered user
 * @param email - email of the registered user
 * @param hash - activation hash
 * @param sendEmailWithVerficationCode - if email with verification code should be sent
 * @param langId - language Id
 */
public void sendMail(String username, String email, String hash, boolean sendEmailWithVerficationCode, Long langId) {
    log.debug("sendMail:: username = {}, email = {}", username, email);
    ensureApplication(langId != null ? langId : getDefaultLang());
    String link = getApp().urlForActivatePage(new PageParameters().add("u", hash));
    if (isSendRegisterEmail()) {
        mailHandler.send(email, getString("512"), RegisterUserTemplate.getEmail(username, email, sendEmailWithVerficationCode ? link : null));
    }
}