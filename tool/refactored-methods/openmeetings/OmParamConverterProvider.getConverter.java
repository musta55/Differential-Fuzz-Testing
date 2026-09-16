@SuppressWarnings("unchecked")
@Override
public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
    return (ParamConverter<T>) CONVERTERS.get(rawType);
}