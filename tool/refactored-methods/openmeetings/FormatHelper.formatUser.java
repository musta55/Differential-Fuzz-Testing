public static String formatUser(User u, boolean isHTMLEscape) {
    if (u == null) {
        return "";
    }
    String formattedUser = formatContactOrNonContactUser(u);
    return isHTMLEscape ? escapeHtml4(formattedUser) : formattedUser;
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatContactOrNonContactUser(User u) {
    if (User.Type.CONTACT == u.getType() && u.getAddress() != null) {
        return String.format("\"%s\" <%s>", u.getDisplayName(), u.getAddress().getEmail());
    }
    return String.format("\"%s\" [%s]", u.getDisplayName(), u.getLogin());
}

