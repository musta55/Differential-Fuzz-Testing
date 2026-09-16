private InvitationTemplate(Locale locale, String invitorName, String message, String link, boolean room) {
    super(locale);
    addLabels(invitorName, message, room);
    addLinks(link, room);
}
// ---- helper method(s) introduced by the refactoring ----
private void addLabels(String invitorName, String message, boolean room) {
    add(new Label("titleLbl", getString("500", getLocale())));
    add(new Label("userLbl", getString("501", getLocale())));
    add(new Label("user", invitorName));
    add(new Label("messageLbl", getString("502", getLocale())));
    add(new Label("message", message).setEscapeModelStrings(false));
}

private void addLinks(String link, boolean room) {
    WebMarkupContainer links = new WebMarkupContainer("links");
    links.add(new Label("comment_for_link1", getString(room ? "template.room.invitation.text" : "template.recording.invitation.text", getLocale())));
    links.add(new ExternalLink("invitation_link1", link).add(new Label("clickMe", getString("504", getLocale()))));
    links.add(new Label("comment_for_link2", getString("505", getLocale())));
    links.add(new Label("invitation_link2", link));
    links.setVisible(link != null);
    add(links);
}

