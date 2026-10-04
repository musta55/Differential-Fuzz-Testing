public static JSONObject parse(Reader reader) throws IOException {
    try {
        if (reader != null) {
            return new JSONObject(reader);
        } else {
            return null;
        }
    } catch (JSONException je) {
        throw new IOException("Error parsing json", je);
    }
}