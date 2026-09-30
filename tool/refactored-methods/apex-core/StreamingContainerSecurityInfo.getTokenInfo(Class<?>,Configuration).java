@Override
public TokenInfo getTokenInfo(Class<?> type, Configuration c) {
    TokenInfo tokenInfo = null;
    if (type.equals(StreamingContainerUmbilicalProtocol.class)) {
        tokenInfo = createTokenInfo();
    }
    return tokenInfo;
}
// ---- helper method(s) introduced by the refactoring ----
private TokenInfo createTokenInfo() {
    return new TokenInfo() {

        @Override
        public Class<? extends TokenSelector<? extends TokenIdentifier>> value() {
            return StramDelegationTokenSelector.class;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    };
}

