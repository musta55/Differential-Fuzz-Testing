private boolean hasStopWords(String password) {
    if (checkWord(password, u.getLogin())) {
        return true;
    }
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