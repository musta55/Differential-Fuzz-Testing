private InvitationTemplate(Locale locale, String invitorName, String message, String link, boolean room) {
    super(locale);
    add(new Label("titleLbl", getString("500", locale)));
    add(new Label("userLbl", getString("501", locale)));
    add(new Label("user", invitorName));
    add(new Label("messageLbl", getString("502", locale)));
    add(new Label("message", message).setEscapeModelStrings(false));
    add(new WebMarkupContainer("links").add(new Label("comment_for_link1", getString(room ? "template.room.invitation.text" : "template.recording.invitation.text", locale))).add(new ExternalLink("invitation_link1", link).add(new Label("clickMe", getString("504", locale)))).add(new Label("comment_for_link2", getString("505", locale))).add(new Label("invitation_link2", link)).setVisible(link != null));
}