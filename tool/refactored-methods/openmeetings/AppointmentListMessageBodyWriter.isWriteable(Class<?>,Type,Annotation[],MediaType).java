@Override
public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
    return isParameterizedTypeOfAppointmentDTO(type);
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

