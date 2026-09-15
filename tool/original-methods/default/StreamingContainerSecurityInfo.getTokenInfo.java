@Override
public TokenInfo getTokenInfo(Class<?> type, Configuration c) {
    TokenInfo tokenInfo = null;
    if (type.equals(StreamingContainerUmbilicalProtocol.class)) {
        tokenInfo = new TokenInfo() {

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
    return tokenInfo;
}