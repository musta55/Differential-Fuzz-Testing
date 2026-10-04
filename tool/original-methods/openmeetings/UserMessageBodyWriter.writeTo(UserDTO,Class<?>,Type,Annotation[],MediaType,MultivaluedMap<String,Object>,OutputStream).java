@Override
public void writeTo(UserDTO t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
    Writer writer = new OutputStreamWriter(out, UTF_8);
    writer.write(new JSONObject().put(ROOT, UserParamConverter.json(t)).toString());
    writer.flush();
}