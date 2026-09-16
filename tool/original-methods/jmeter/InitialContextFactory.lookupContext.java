/**
 * Look up the context from the local cache, creating it if necessary.
 *
 * @param initialContextFactory used to set the property {@link Context#INITIAL_CONTEXT_FACTORY}
 * @param providerUrl used to set the property {@link Context#PROVIDER_URL}
 * @param useAuth set <code>true</code> if security is to be used.
 * @param securityPrincipal used to set the property {@link Context#SECURITY_PRINCIPAL}
 * @param securityCredentials used to set the property {@link Context#SECURITY_CREDENTIALS}
 * @return the context, never <code>null</code>
 * @throws NamingException when creation of the context fails
 */
public static Context lookupContext(String initialContextFactory, String providerUrl, boolean useAuth, String securityPrincipal, String securityCredentials) throws NamingException {
    String cacheKey = createKey(Thread.currentThread().getId(), initialContextFactory, providerUrl, securityPrincipal, securityCredentials);
    Context ctx = MAP.get(cacheKey);
    if (ctx == null) {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        props.setProperty(Context.PROVIDER_URL, providerUrl);
        if (useAuth && securityPrincipal != null && securityCredentials != null && securityPrincipal.length() > 0 && securityCredentials.length() > 0) {
            props.setProperty(Context.SECURITY_PRINCIPAL, securityPrincipal);
            props.setProperty(Context.SECURITY_CREDENTIALS, securityCredentials);
            log.info("authentication properties set");
        }
        try {
            ctx = new InitialContext(props);
        } catch (NoClassDefFoundError | Exception e) {
            throw new NamingException(e.toString());
        }
        // we want to return the context that is actually in the map
        // if it's the first put we will have a null result
        Context oldCtx = MAP.putIfAbsent(cacheKey, ctx);
        if (oldCtx != null) {
            // There was an object in map, destroy the temporary and return one in map (oldCtx)
            try {
                ctx.close();
            } catch (Exception e) {
                // NOOP
            }
            ctx = oldCtx;
        }
        // else No object in Map, ctx is the one
    }
    return ctx;
}