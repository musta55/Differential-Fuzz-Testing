public synchronized boolean isValidRequest(String token) {
    if (token == null) {
        return this.allowPostRequestWithoutDoubleSubmitPrevention;
    }
    String previousToken = this.currentToken;
    createNewToken();
    return token.equals(previousToken);
}