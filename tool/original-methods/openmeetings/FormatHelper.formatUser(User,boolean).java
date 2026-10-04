public static String formatUser(User u, boolean isHTMLEscape) {
    String user = "";
    if (u != null) {
        if (User.Type.CONTACT == u.getType() && u.getAddress() != null) {
            user = String.format("\"%s\" <%s>", u.getDisplayName(), u.getAddress().getEmail());
        } else {
            user = String.format("\"%s\" [%s]", u.getDisplayName(), u.getLogin());
        }
        user = isHTMLEscape ? escapeHtml4(user) : user;
    }
    return user;
}