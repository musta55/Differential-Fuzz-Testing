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
    if (getRequestorType() == RequestorType.PROXY) {
        return new PasswordAuthentication(userName, password);
    }
    return null;
}