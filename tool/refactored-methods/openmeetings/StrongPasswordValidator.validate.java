@Override
public void validate(IValidatable<String> pass) {
    String password = pass.getValue();
    if (badLength(password)) {
        error(pass, "bad.password.short", Map.of("0", getMinPasswdLength()));
    }
    if (noLowerCase(password)) {
        error(pass, "bad.password.lower");
    }
    if (noUpperCase(password)) {
        error(pass, "bad.password.upper");
    }
    if (noDigit(password)) {
        error(pass, "bad.password.digit");
    }
    if (noSymbol(password)) {
        error(pass, "bad.password.special");
    }
    if (hasStopWords(password)) {
        error(pass, "bad.password.stop");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean hasStopWordsInLogin(String password) {
    return checkWord(password, u.getLogin());
}

private boolean hasStopWordsInEmail(String password) {
    if (u.getAddress() != null) {
        String email = u.getAddress().getEmail();
        if (!Strings.isEmpty(email)) {
            for (String part : email.split("[.@]")) {
                if (checkWord(password, part)) {
                    return true;
                }
            }
        }
    }
    return false;
}

