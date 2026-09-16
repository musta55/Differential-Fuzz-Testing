public synchronized boolean isValidRequest(String token) {
    if (token == null) {
        return this.allowPostRequestWithoutDoubleSubmitPrevention;
    }
    String previousToken = this.currentToken;
    createNewToken();
    return isTokenValid(token, previousToken);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isTokenValid(String token, String previousToken) {
    return token.equals(previousToken);
}

