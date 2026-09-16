/**
 * OAuth constructor
 *
 * @param jsonStr - json data from server as string
 * @param server - {@link OAuthServer} to get mapping
 */
public OAuthUser(String jsonStr, OAuthServer server) {
    Map<String, String> data = extractUserData(jsonStr, server.getMapping());
    String login = data.get(PARAM_LOGIN);
    String email = data.get(PARAM_EMAIL);
    if (Strings.isEmpty(email)) {
        email = generateEmailFromLogin(login, server);
        data.put(PARAM_EMAIL, email);
    }
    userData = Collections.unmodifiableMap(data);
}
// ---- helper method(s) introduced by the refactoring ----
private Map<String, String> extractUserData(String jsonStr, Map<String, String> mapping) {
    JSONObject json = parseJson(jsonStr, mapping.get(PARAM_LOGIN));
    Map<String, String> data = new HashMap<>();
    for (Map.Entry<String, String> entry : mapping.entrySet()) {
        data.put(entry.getKey(), json.optString(entry.getValue()));
    }
    return data;
}

private String generateEmailFromLogin(String login, OAuthServer server) {
    try {
        return String.format("%s@%s", login, new URL(server.getIconUrl()).getHost());
    } catch (MalformedURLException e) {
        log.error("Failed to generate user email from login: {}", login, e);
        return "";
    }
}

private static JSONObject parseJson(String str, String prop) {
    JSONObject json = new JSONObject(str);
    return parseJson(json, prop);
}

private static JSONObject parseJson(JSONObject json, String prop) {
    if (json.has(prop)) {
        return json;
    }
    for (String key : json.keySet()) {
        Object o = json.get(key);
        if (o instanceof JSONArray ja) {
            for (int i = 0; i < ja.length(); ++i) {
                JSONObject jao = ja.getJSONObject(i);
                JSONObject res = parseJson(jao, prop);
                if (res != null) {
                    return res;
                }
            }
        } else if (o instanceof JSONObject jo) {
            JSONObject res = parseJson(jo, prop);
            if (res != null) {
                return res;
            }
        }
    }
    return null;
}

