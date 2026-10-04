/**
 * OAuth constructor
 *
 * @param jsonStr - json data from server as string
 * @param server - {@link OAuthServer} to get mapping
 */
public OAuthUser(String jsonStr, OAuthServer server) {
    // get attributes names
    // we expect login mapping always in place
    JSONObject json = getJSON(jsonStr, server.getMapping().get(PARAM_LOGIN));
    Map<String, String> data = new HashMap<>();
    for (Map.Entry<String, String> entry : server.getMapping().entrySet()) {
        data.put(entry.getKey(), json.optString(entry.getValue()));
    }
    String login = data.get(PARAM_LOGIN);
    String email = data.get(PARAM_EMAIL);
    if (Strings.isEmpty(email)) {
        try {
            data.put(PARAM_EMAIL, String.format("%s@%s", login, new URL(server.getIconUrl()).getHost()));
        } catch (MalformedURLException e) {
            log.error("Failed to get user email from JSON: {}", json);
        }
    }
    userData = Collections.unmodifiableMap(data);
}