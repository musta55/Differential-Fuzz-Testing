/**
 * Create a publisher using either the jndi.properties file or the provided
 * parameters
 *
 * @param useProps
 *            true if a jndi.properties file is to be used
 * @param initialContextFactory
 *            the (ignored if useProps is true)
 * @param providerUrl
 *            (ignored if useProps is true)
 * @param connfactory
 *            name of the object factory to lookup in context
 * @param destinationName
 *            name of the destination to use
 * @param useAuth
 *            (ignored if useProps is true)
 * @param securityPrincipal
 *            (ignored if useProps is true)
 * @param securityCredentials
 *            (ignored if useProps is true)
 * @param staticDestination
 *            true if the destination is not to change between loops
 * @throws JMSException
 *             if the context could not be initialised, or there was some
 *             other error
 * @throws NamingException
 *             when creation of the publisher fails
 */
public Publisher(boolean useProps, String initialContextFactory, String providerUrl, String connfactory, String destinationName, boolean useAuth, String securityPrincipal, String securityCredentials, boolean staticDestination) throws JMSException, NamingException {
    super();
    boolean initSuccess = false;
    try {
        ctx = InitialContextFactory.getContext(useProps, initialContextFactory, providerUrl, useAuth, securityPrincipal, securityCredentials);
        connection = Utils.getConnection(ctx, connfactory);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        staticDest = staticDestination;
        if (staticDest) {
            Destination dest = Utils.lookupDestination(ctx, destinationName);
            producer = session.createProducer(dest);
        } else {
            producer = session.createProducer(null);
        }
        initSuccess = true;
    } finally {
        if (!initSuccess) {
            close();
        }
    }
}