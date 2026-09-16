@SuppressWarnings("unchecked")
@Override
public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
    if (Calendar.class.isAssignableFrom(rawType)) {
        return (ParamConverter<T>) new CalendarParamConverter();
    } else if (Date.class.isAssignableFrom(rawType)) {
        return (ParamConverter<T>) new DateParamConverter();
    } else if (AppointmentDTO.class.isAssignableFrom(rawType)) {
        return (ParamConverter<T>) new AppointmentParamConverter();
    } else if (UserDTO.class.isAssignableFrom(rawType)) {
        return (ParamConverter<T>) new UserParamConverter();
    }
    return null;
}