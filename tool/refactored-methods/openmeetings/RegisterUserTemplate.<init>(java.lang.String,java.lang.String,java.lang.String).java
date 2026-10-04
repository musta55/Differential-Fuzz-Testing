private RegisterUserTemplate(String username, String email, String verificationUrl) {
    super(getOmSession().getLocale());
    addUsernameLabel(username);
    addEmailLabel(email);
    addVerificationContainer(verificationUrl);
    addGroupLabel();
    addApplicationLink();
}
// ---- helper method(s) introduced by the refactoring ----
private void addUsernameLabel(String username) {
    add(new Label("username", username));
}

private void addEmailLabel(String email) {
    add(new Label("email", email));
}

private void addVerificationContainer(String verificationUrl) {
    WebMarkupContainer verification = new WebMarkupContainer("verification");
    verification.add(new Label("verification_url2", verificationUrl));
    verification.add(new ExternalLink("verification_url1", verificationUrl));
    verification.setVisible(verificationUrl != null);
    add(verification);
}

private void addGroupLabel() {
    add(new Label("groupLbl", getString("511", locale)));
}

private void addApplicationLink() {
    add(new ExternalLink("url", getBaseUrl(), getApplicationName()));
}

