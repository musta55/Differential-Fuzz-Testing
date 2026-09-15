@Override
public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    if (!(req instanceof HttpServletRequest)) {
        throw new ServletException("This filter only works for HTTP/HTTPS");
    }
    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpServletResponse httpResp = (HttpServletResponse) resp;
    String remoteAddr = httpReq.getRemoteAddr();
    String requestURI = httpReq.getRequestURI();
    String user = handleProxyAuthentication(httpReq, httpResp, requestURI, remoteAddr);
    if (user == null) {
        user = handleCookieAuthentication(httpReq, httpResp, remoteAddr);
    }
    if (user == null) {
        logger.debug("{}: could not find user, so user principal will not be set", remoteAddr);
        chain.doFilter(req, resp);
    } else {
        final StramWSPrincipal principal = new StramWSPrincipal(user);
        ServletRequest requestWrapper = new StramWSServletRequestWrapper(httpReq, principal);
        chain.doFilter(requestWrapper, resp);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private String handleProxyAuthentication(HttpServletRequest httpReq, HttpServletResponse httpResp, String requestURI, String remoteAddr) throws ServletException, IOException {
    if (!getProxyAddresses().contains(httpReq.getRemoteAddr())) {
        return null;
    }
    String user = getUserFromProxyCookie(httpReq);
    if (requestURI.equals(WebServices.PATH) && user != null) {
        String token = createClientToken(user, httpReq.getLocalAddr());
        logger.debug("{}: creating token {}", remoteAddr, token);
        Cookie cookie = new Cookie(CLIENT_COOKIE, token);
        httpResp.addCookie(cookie);
    } else {
        logger.info("{}: proxy access to URI {} by user {}, no cookie created", remoteAddr, requestURI, user);
    }
    return user;
}

private String handleCookieAuthentication(HttpServletRequest httpReq, HttpServletResponse httpResp, String remoteAddr) throws IOException {
    Cookie cookie = getClientCookie(httpReq);
    if (cookie == null) {
        logger.debug("{}: cookie not found {}", remoteAddr, CLIENT_COOKIE);
        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }
    String user = verifyClientToken(cookie.getValue(), remoteAddr);
    if (user == null) {
        logger.debug("{}: invalid cookie {}", remoteAddr, cookie.getValue());
        httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }
    return user;
}

private String getUserFromProxyCookie(HttpServletRequest httpReq) {
    if (httpReq.getCookies() == null) {
        return null;
    }
    for (Cookie c : httpReq.getCookies()) {
        if (WEBAPP_PROXY_USER.equals(c.getName())) {
            return c.getValue();
        }
    }
    return null;
}

private Cookie getClientCookie(HttpServletRequest httpReq) {
    if (httpReq.getCookies() == null) {
        return null;
    }
    for (Cookie c : httpReq.getCookies()) {
        if (CLIENT_COOKIE.equals(c.getName())) {
            return c;
        }
    }
    return null;
}

