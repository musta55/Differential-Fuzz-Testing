@Override
public void writeTo(List<AppointmentDTO> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
    Writer writer = new OutputStreamWriter(out, UTF_8);
    JSONArray rr = new JSONArray();
    for (AppointmentDTO dto : t) {
        rr.put(AppointmentParamConverter.json(dto));
    }
    writer.write(new JSONObject().put(ROOT, rr).toString());
    writer.flush();
}