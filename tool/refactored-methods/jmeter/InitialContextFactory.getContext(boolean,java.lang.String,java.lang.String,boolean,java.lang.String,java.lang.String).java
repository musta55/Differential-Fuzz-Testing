/**
 * Initialize the JNDI initial context
 *
 * @param useProps
 *            if true, create a new InitialContext; otherwise use the other
 *            parameters to call
 *            {@link #lookupContext(String, String, boolean, String, String)}
 * @param initialContextFactory
 *            name of the initial context factory (ignored if
 *            <code>useProps</code> is <code>true</code>)
 * @param providerUrl
 *            url of the provider to use (ignored if <code>useProps</code>
 *            is <code>true</code>)
 * @param useAuth
 *            <code>true</code> if auth should be used, <code>false</code>
 *            otherwise (ignored if <code>useProps</code> is
 *            <code>true</code>)
 * @param securityPrincipal
 *            name of the principal to (ignored if <code>useProps</code> is
 *            <code>true</code>)
 * @param securityCredentials
 *            credentials for the principal (ignored if
 *            <code>useProps</code> is <code>true</code>)
 * @return the context, never <code>null</code>
 * @throws NamingException
 *             when creation of the context fails
 */
public static Context getContext(boolean useProps, String initialContextFactory, String providerUrl, boolean useAuth, String securityPrincipal, String securityCredentials) throws NamingException {
    if (useProps) {
        return createNewContext();
    } else {
        return lookupContext(initialContextFactory, providerUrl, useAuth, securityPrincipal, securityCredentials);
    }
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

