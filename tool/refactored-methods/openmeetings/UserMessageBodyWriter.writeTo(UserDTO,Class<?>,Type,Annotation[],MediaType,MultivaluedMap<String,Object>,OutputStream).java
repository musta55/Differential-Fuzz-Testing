@Override
public void writeTo(UserDTO t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
    Writer writer = new OutputStreamWriter(out, UTF_8);
    writeJsonObject(writer, t);
    writer.flush();
}
// ---- helper method(s) introduced by the refactoring ----
private void writeJsonObject(Writer writer, UserDTO userDTO) throws IOException {
    writer.write(new JSONObject().put(ROOT, UserParamConverter.json(userDTO)).toString());
}

