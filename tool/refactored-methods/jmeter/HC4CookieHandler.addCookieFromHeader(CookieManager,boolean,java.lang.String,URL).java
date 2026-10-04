@Override
@SuppressWarnings("JavaUtilDate")
public void addCookieFromHeader(CookieManager cookieManager, boolean checkCookies, String cookieHeader, URL url) {
    boolean debugEnabled = log.isDebugEnabled();
    if (debugEnabled) {
        log.debug("Received Cookie: {} From: {}", cookieHeader, url.toExternalForm());
    }
    String protocol = url.getProtocol();
    String host = url.getHost();
    int port = HTTPSamplerBase.getDefaultPort(protocol, url.getPort());
    String path = url.getPath();
    boolean isSecure = HTTPSamplerBase.isSecure(protocol);
    List<org.apache.http.cookie.Cookie> cookies = null;
    CookieOrigin cookieOrigin = new CookieOrigin(host, port, path, isSecure);
    BasicHeader basicHeader = new BasicHeader(HTTPConstants.HEADER_SET_COOKIE, cookieHeader);
    try {
        cookies = cookieSpec.parse(basicHeader, cookieOrigin);
    } catch (MalformedCookieException e) {
        log.error("Unable to add the cookie", e);
    }
    if (cookies == null) {
        return;
    }
    for (org.apache.http.cookie.Cookie cookie : cookies) {
        try {
            if (checkCookies) {
                try {
                    cookieSpec.validate(cookie, cookieOrigin);
                } catch (MalformedCookieException e) {
                    // This means the cookie was wrong for the URL
                    log.info("Not storing invalid cookie: <{}> for URL {} ({})", cookieHeader, url, e.getLocalizedMessage());
                    continue;
                }
            }
            Instant expiryInstant = cookie.getExpiryDate() != null ? cookie.getExpiryDate().toInstant() : null;
            long exp = expiryInstant != null ? expiryInstant.getEpochSecond() : 0;
            Cookie newCookie = new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.isSecure(), exp, ((BasicClientCookie) cookie).containsAttribute(ClientCookie.PATH_ATTR), ((BasicClientCookie) cookie).containsAttribute(ClientCookie.DOMAIN_ATTR), cookie.getVersion());
            // Store session cookies as well as unexpired ones
            if (exp == 0 || expiryInstant.toEpochMilli() >= System.currentTimeMillis()) {
                // Has its own debug log; removes matching cookies
                cookieManager.add(newCookie);
            } else {
                cookieManager.removeMatchingCookies(newCookie);
                if (debugEnabled) {
                    log.info("Dropping expired Cookie: {}", newCookie);
                }
            }
        } catch (IllegalArgumentException e) {
            log.warn(cookieHeader + e.getLocalizedMessage());
        }
    }
}