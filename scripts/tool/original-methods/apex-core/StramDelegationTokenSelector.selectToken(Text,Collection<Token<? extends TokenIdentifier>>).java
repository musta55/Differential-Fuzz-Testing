@Override
public Token<StramDelegationTokenIdentifier> selectToken(Text text, Collection<Token<? extends TokenIdentifier>> clctn) {
    Token<StramDelegationTokenIdentifier> token = null;
    if (text != null) {
        for (Token<? extends TokenIdentifier> ctoken : clctn) {
            if (StramDelegationTokenIdentifier.IDENTIFIER_KIND.equals(ctoken.getKind()) && text.equals(ctoken.getService())) {
                token = (Token<StramDelegationTokenIdentifier>) ctoken;
            }
        }
    }
    return token;
}