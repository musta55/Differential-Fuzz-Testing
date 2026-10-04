/**
 * Convenience method for adding URLs to the collection. If the url
 * parameter is <code>null</code>, empty or URL is malformed, nothing is
 * done
 *
 * @param url
 *            String, may be null or empty
 * @param baseUrl
 *            base for <code>url</code> to add information, which might be
 *            missing in <code>url</code>
 * @return boolean condition returned by the add() method of the underlying
 *         collection
 */
public boolean addURL(String url, URL baseUrl) {
    if (url == null || url.length() == 0) {
        return false;
    }
    url = StringEscapeUtils.unescapeXml(url);
    boolean b;
    try {
        b = this.add(ConversionUtils.makeRelativeURL(baseUrl, url));
    } catch (MalformedURLException mfue) {
        // No WARN message to avoid performance impact
        if (log.isDebugEnabled()) {
            log.debug("Error occurred building relative url for: {}, message: {}", url, mfue.getMessage());
        }
        // No point in adding the URL as String as it will result in null
        // returned during iteration, see URLString
        // See https://bz.apache.org/bugzilla/show_bug.cgi?id=55092
        return false;
    }
    return b;
}