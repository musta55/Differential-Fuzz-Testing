/**
 * Return a PasswordAuthentication instance using the userName and password
 * specified in the constructor.
 * Only applies to PROXY request types.
 *
 * @return a PasswordAuthentication instance to use for authenticating with
 *         the proxy
 */
@Override
protected PasswordAuthentication getPasswordAuthentication() {
    switch(getRequestorType()) {
        case PROXY:
            return new PasswordAuthentication(userName, password);
        case SERVER:
            break;
    }
    return null;
}