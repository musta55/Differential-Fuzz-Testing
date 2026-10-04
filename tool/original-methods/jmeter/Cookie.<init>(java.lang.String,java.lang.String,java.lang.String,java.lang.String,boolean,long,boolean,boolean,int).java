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
    this.setName(name);
    this.setValue(value);
    this.setDomain(domain);
    this.setPath(path);
    this.setSecure(secure);
    this.setExpires(expires);
    this.setPathSpecified(hasPath);
    this.setDomainSpecified(hasDomain);
    this.setVersion(version);
}