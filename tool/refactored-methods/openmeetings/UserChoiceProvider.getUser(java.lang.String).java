public User getUser(String value) {
    if (Strings.isEmpty(value)) {
        return null;
    }
    ParsedEmail parsedEmail = parseEmail(value);
    if (parsedEmail == null || Strings.isEmpty(parsedEmail.email)) {
        return null;
    }
    if (!isValidEmail(parsedEmail.email)) {
        return null;
    }
    return userDao.getContact(parsedEmail.email, parsedEmail.firstName, parsedEmail.lastName, getUserId());
}
// ---- helper method(s) introduced by the refactoring ----
private ParsedEmail parseEmail(String value) {
    int idx = value.indexOf('<');
    if (idx > -1) {
        int idx1 = value.indexOf('>', idx);
        if (idx1 > -1) {
            String email = value.substring(idx + 1, idx1);
            String name = value.substring(0, idx).replace("\"", "");
            int idx2 = name.indexOf(' ');
            String firstName = idx2 > -1 ? name.substring(0, idx2) : "";
            String lastName = idx2 > -1 ? name.substring(idx2 + 1) : name;
            return new ParsedEmail(email, firstName, lastName);
        }
    }
    return new ParsedEmail(value, "", "");
}

private boolean isValidEmail(String email) {
    Validatable<String> valEmail = new Validatable<>(email);
    RfcCompliantEmailAddressValidator.getInstance().validate(valEmail);
    return valEmail.isValid();
}

