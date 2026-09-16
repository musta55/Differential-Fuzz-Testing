@Override
public AppointmentDTO readFrom(Class<AppointmentDTO> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(entityStream, UTF_8));
    String line;
    StringBuilder sb = new StringBuilder();
    while ((line = br.readLine()) != null) {
        sb.append(line).append(System.lineSeparator());
    }
    return new AppointmentParamConverter().fromString(sb.toString());
}