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
        try {
            return new InitialContext();
        } catch (NoClassDefFoundError | Exception e) {
            throw new NamingException(e.toString());
        }
    } else {
        return lookupContext(initialContextFactory, providerUrl, useAuth, securityPrincipal, securityCredentials);
    }
}