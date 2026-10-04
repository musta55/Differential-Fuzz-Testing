@Override
public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
    if (type instanceof ParameterizedType pt) {
        Type[] args = pt.getActualTypeArguments();
        if (args != null && args.length == 1) {
            return AppointmentDTO.class.equals(args[0]);
        }
    }
    return false;
}