@Override
public AppointmentDTO readFrom(Class<AppointmentDTO> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(entityStream, UTF_8))) {
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(line -> sb.append(line).append(System.lineSeparator()));
        return new AppointmentParamConverter().fromString(sb.toString());
    }
}