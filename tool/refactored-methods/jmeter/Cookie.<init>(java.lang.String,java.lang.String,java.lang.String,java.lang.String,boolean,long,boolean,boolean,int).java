/**
 * Create a JMeter Cookie.
 *
 * @param name name of the cookie
 * @param value value of the cookie
 * @param domain domain for which the cookie is valid
 * @param path path for which the cookie is valid
 * @param secure flag whether cookie is to be handled as 'secure'
 * @param expires - this is in seconds
 * @param hasPath - was the path explicitly specified?
 * @param hasDomain - was the domain explicitly specified?
 * @param version - cookie spec. version
 */
public Cookie(String name, String value, String domain, String path, boolean secure, long expires, boolean hasPath, boolean hasDomain, int version) {
    setName(name);
    setValue(value);
    setDomain(domain);
    setPath(path);
    setSecure(secure);
    setExpires(expires);
    setPathSpecified(hasPath);
    setDomainSpecified(hasDomain);
    setVersion(version);
}