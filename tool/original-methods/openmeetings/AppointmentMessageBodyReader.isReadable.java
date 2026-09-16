@Override
public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
    return type.equals(AppointmentDTO.class);
}