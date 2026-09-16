/**
 * Constructor
 */
AnnotatedConstructorImpl(AnnotatedTypeImpl<X> type, Constructor<?> constructor, AnnotationStore annotations, Map<Integer, AnnotationStore> parameterAnnotations, Map<Integer, Type> typeOverrides) {
    super(type, (Constructor<X>) constructor, constructor.getDeclaringClass(), constructor.getParameterTypes(), getGenericArray(constructor), annotations, parameterAnnotations, null, typeOverrides);
}