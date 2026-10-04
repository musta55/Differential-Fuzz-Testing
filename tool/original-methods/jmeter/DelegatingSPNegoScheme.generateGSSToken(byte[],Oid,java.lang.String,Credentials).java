@Override
protected byte[] generateGSSToken(final byte[] input, final Oid oid, final String authServer, final Credentials credentials) throws GSSException {
    final GSSManager manager = getManager();
    final GSSName serverName = manager.createName("HTTP@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
    final GSSCredential gssCredential;
    if (credentials instanceof KerberosCredentials) {
        gssCredential = ((KerberosCredentials) credentials).getGSSCredential();
    } else {
        gssCredential = null;
    }
    final GSSContext gssContext = createDelegatingGSSContext(manager, oid, serverName, gssCredential);
    try {
        if (input != null) {
            return gssContext.initSecContext(input, 0, input.length);
        } else {
            return gssContext.initSecContext(new byte[] {}, 0, 0);
        }
    } finally {
        gssContext.dispose();
    }
}