@Override
public void handleMessage(Message outMessage) {
    final String allowOrigin = getRestAllowOrigin();
    if (!Strings.isEmpty(allowOrigin)) {
        setHeader(outMessage, "Access-Control-Allow-Origin", allowOrigin);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setHeader(Message message, String headerName, String headerValue) {
    @SuppressWarnings("unchecked")
    Map<String, List<String>> headers = (Map<String, List<String>>) message.computeIfAbsent(Message.PROTOCOL_HEADERS, key -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
    headers.put(headerName, List.of(headerValue));
}

