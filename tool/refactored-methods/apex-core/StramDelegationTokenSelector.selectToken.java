@Override
public Token<StramDelegationTokenIdentifier> selectToken(Text text, Collection<Token<? extends TokenIdentifier>> clctn) {
    return findToken(text, clctn);
}
// ---- helper method(s) introduced by the refactoring ----
private Token<StramDelegationTokenIdentifier> findToken(Text text, Collection<Token<? extends TokenIdentifier>> clctn) {
    if (text == null) {
        return null;
    }
    for (Token<? extends TokenIdentifier> ctoken : clctn) {
        if (StramDelegationTokenIdentifier.IDENTIFIER_KIND.equals(ctoken.getKind()) && text.equals(ctoken.getService())) {
            return (Token<StramDelegationTokenIdentifier>) ctoken;
        }
    }
    return null;
}

