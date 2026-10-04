@Override
public void writeTo(AppointmentDTO t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
    Writer writer = new OutputStreamWriter(out, UTF_8);
    writer.write(new JSONObject().put(ROOT, AppointmentParamConverter.json(t)).toString());
    writer.flush();
}