private RegisterUserTemplate(String username, String email, String verificationUrl) {
    super(getOmSession().getLocale());
    add(new Label("registrationLbl", getString("506", locale)));
    add(new Label("username", username));
    add(new Label("email", email));
    WebMarkupContainer verification = new WebMarkupContainer("verification");
    add(verification.add(new Label("verification_url2", verificationUrl)).add(new ExternalLink("verification_url1", verificationUrl)).setVisible(verificationUrl != null));
    add(new Label("groupLbl", getString("511", locale)));
    add(new ExternalLink("url", getBaseUrl(), getApplicationName()));
}