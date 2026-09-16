@Override
protected byte[] generateGSSToken(final byte[] input, final Oid oid, final String authServer, final Credentials credentials) throws GSSException {
    final GSSManager manager = getManager();
    final GSSName serverName = createServerName(manager, authServer);
    final GSSCredential gssCredential = extractGSSCredential(credentials);
    final GSSContext gssContext = createDelegatingGSSContext(manager, oid, serverName, gssCredential);
    try {
        return gssContext.initSecContext(input != null ? input : new byte[] {}, 0, input != null ? input.length : 0);
    } finally {
        gssContext.dispose();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static GSSName createServerName(final GSSManager manager, final String authServer) throws GSSException {
    return manager.createName("HTTP@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
}

private static GSSCredential extractGSSCredential(final Credentials credentials) {
    return credentials instanceof KerberosCredentials ? ((KerberosCredentials) credentials).getGSSCredential() : null;
}

