@Override
public void handleMessage(Message outMessage) {
    final String allowOrigin = getRestAllowOrigin();
    if (!Strings.isEmpty(allowOrigin)) {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers = (Map<String, List<String>>) outMessage.computeIfAbsent(Message.PROTOCOL_HEADERS, key -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
        headers.put("Access-Control-Allow-Origin", List.of(allowOrigin));
    }
}