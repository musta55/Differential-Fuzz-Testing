@Override
public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
    return AppointmentDTO.class.equals(type);
}