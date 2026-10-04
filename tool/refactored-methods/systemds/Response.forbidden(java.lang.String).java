public static FullHttpResponse forbidden(final String exception) {
    return createResponse(HttpResponseStatus.FORBIDDEN, exception, "text/plain");
}
// ---- helper method(s) introduced by the refactoring ----
private static FullHttpResponse createResponse(HttpResponseStatus status, String message, String contentType) {
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(message.getBytes()));
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    return response;
}

