public static JSONObject parse(Reader reader) throws IOException {
    try {
        if (reader != null) {
            JSONObject result = new JSONObject(reader);
            return result;
        } else {
            return null;
        }
    } catch (JSONException je) {
        throw new IOException("Error parsing json", je);
    }
}