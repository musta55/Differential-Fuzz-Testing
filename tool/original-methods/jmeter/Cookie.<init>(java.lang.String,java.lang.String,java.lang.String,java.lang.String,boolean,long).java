/**
 * create the cookie
 * @param name name of the cookie
 * @param value value of the cookie
 * @param domain domain for which the cookie is valid
 * @param path  path for which the cookie is valid
 * @param secure flag whether cookie is to be handled as 'secure'
 * @param expires - this is in seconds
 */
public Cookie(String name, String value, String domain, String path, boolean secure, long expires) {
    this(name, value, domain, path, secure, expires, true, true);
}