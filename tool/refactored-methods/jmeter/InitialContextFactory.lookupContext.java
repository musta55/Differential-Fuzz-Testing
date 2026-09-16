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
    String cacheKey = createCacheKey(initialContextFactory, providerUrl, securityPrincipal, securityCredentials);
    Context ctx = MAP.get(cacheKey);
    if (ctx == null) {
        Properties props = createProperties(initialContextFactory, providerUrl, useAuth, securityPrincipal, securityCredentials);
        ctx = createContext(props);
        Context oldCtx = MAP.putIfAbsent(cacheKey, ctx);
        if (oldCtx != null) {
            closeContext(ctx);
            ctx = oldCtx;
        }
    }
    return ctx;
}
// ---- helper method(s) introduced by the refactoring ----
private static String createCacheKey(String initialContextFactory, String providerUrl, String securityPrincipal, String securityCredentials) {
    StringBuilder builder = new StringBuilder();
    builder.append(Thread.currentThread().getId()).append("#").append(initialContextFactory).append("#").append(providerUrl).append("#");
    if (!StringUtils.isEmpty(securityPrincipal)) {
        builder.append(securityPrincipal).append("#");
    }
    if (!StringUtils.isEmpty(securityCredentials)) {
        builder.append(securityCredentials);
    }
    return builder.toString();
}

private static Properties createProperties(String initialContextFactory, String providerUrl, boolean useAuth, String securityPrincipal, String securityCredentials) {
    Properties props = new Properties();
    props.setProperty(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
    props.setProperty(Context.PROVIDER_URL, providerUrl);
    if (useAuth && StringUtils.isNotEmpty(securityPrincipal) && StringUtils.isNotEmpty(securityCredentials)) {
        props.setProperty(Context.SECURITY_PRINCIPAL, securityPrincipal);
        props.setProperty(Context.SECURITY_CREDENTIALS, securityCredentials);
        log.info("authentication properties set");
    }
    return props;
}

private static Context createContext(Properties props) throws NamingException {
    try {
        return new InitialContext(props);
    } catch (NoClassDefFoundError | Exception e) {
        throw new NamingException(e.toString());
    }
}

private static void closeContext(Context ctx) {
    try {
        ctx.close();
    } catch (Exception e) {
        // NOOP
    }
}

private static Context createNewContext() throws NamingException {
    try {
        return new InitialContext();
    } catch (NoClassDefFoundError | Exception e) {
        throw new NamingException(e.toString());
    }
}

