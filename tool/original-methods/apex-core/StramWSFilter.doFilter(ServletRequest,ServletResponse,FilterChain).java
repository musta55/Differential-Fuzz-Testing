@Override
public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    if (!(req instanceof HttpServletRequest)) {
        throw new ServletException("This filter only works for HTTP/HTTPS");
    }
    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpServletResponse httpResp = (HttpServletResponse) resp;
    String remoteAddr = httpReq.getRemoteAddr();
    String requestURI = httpReq.getRequestURI();
    boolean authenticate = true;
    String user = null;
    if (getProxyAddresses().contains(httpReq.getRemoteAddr())) {
        if (httpReq.getCookies() != null) {
            for (Cookie c : httpReq.getCookies()) {
                if (WEBAPP_PROXY_USER.equals(c.getName())) {
                    user = c.getValue();
                    break;
                }
            }
        }
        if (requestURI.equals(WebServices.PATH) && (user != null)) {
            String token = createClientToken(user, httpReq.getLocalAddr());
            logger.debug("{}: creating token {}", remoteAddr, token);
            Cookie cookie = new Cookie(CLIENT_COOKIE, token);
            httpResp.addCookie(cookie);
        } else {
            logger.info("{}: proxy access to URI {} by user {}, no cookie created", remoteAddr, requestURI, user);
        }
        authenticate = false;
    }
    if (authenticate) {
        Cookie cookie = null;
        if (httpReq.getCookies() != null) {
            for (Cookie c : httpReq.getCookies()) {
                if (c.getName().equals(CLIENT_COOKIE)) {
                    cookie = c;
                    break;
                }
            }
        }
        boolean valid = false;
        if (cookie != null) {
            user = verifyClientToken(cookie.getValue(), remoteAddr);
            if (user != null) {
                valid = true;
            } else {
                logger.debug("{}: invalid cookie {}", remoteAddr, cookie.getValue());
            }
        } else {
            logger.debug("{}: cookie not found {}", remoteAddr, CLIENT_COOKIE);
        }
        if (!valid) {
            logger.debug("{}: auth failure", remoteAddr);
            httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
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