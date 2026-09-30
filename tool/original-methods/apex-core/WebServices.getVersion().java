@GET
@Produces(MediaType.APPLICATION_JSON)
public JSONObject getVersion() throws JSONException {
    return new JSONObject("{\"version\": \"" + VERSION + "\"}");
}