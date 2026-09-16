/**
 * clear all the InitialContext objects.
 */
public static void close() {
    for (Context ctx : MAP.values()) {
        closeContext(ctx);
    }
    MAP.clear();
    log.info("InitialContextFactory.close() called and Context instances cleaned up");
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

