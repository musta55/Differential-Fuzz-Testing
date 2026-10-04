private boolean hasStopWords(String password) {
    return hasStopWordsInLogin(password) || hasStopWordsInEmail(password);
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

