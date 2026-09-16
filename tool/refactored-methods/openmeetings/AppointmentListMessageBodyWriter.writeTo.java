@Override
public void writeTo(List<AppointmentDTO> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
    Writer writer = new OutputStreamWriter(out, UTF_8);
    String jsonContent = createJsonContent(t);
    writer.write(jsonContent);
    writer.flush();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isParameterizedTypeOfAppointmentDTO(Type type) {
    if (type instanceof ParameterizedType pt) {
        Type[] args = pt.getActualTypeArguments();
        return args != null && args.length == 1 && AppointmentDTO.class.equals(args[0]);
    }
    return false;
}

private String createJsonContent(List<AppointmentDTO> t) {
    JSONArray jsonArray = new JSONArray();
    for (AppointmentDTO dto : t) {
        jsonArray.put(AppointmentParamConverter.json(dto));
    }
    return new JSONObject().put(ROOT, jsonArray).toString();
}

