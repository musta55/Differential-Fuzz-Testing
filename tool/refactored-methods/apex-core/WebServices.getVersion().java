@GET
@Produces(MediaType.APPLICATION_JSON)
public JSONObject getVersion() throws JSONException {
    JSONObject versionJson = new JSONObject();
    versionJson.put("version", VERSION);
    return versionJson;
}