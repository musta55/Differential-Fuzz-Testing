@Override
protected Converter resolveInstanceForClass(FacesContext facesContext, Class<?> wrappedClass) {
    FacesConverter facesConverter = getFacesConverterAnnotation(wrappedClass);
    if (facesConverter == null) {
        return null;
    }
    String converterId = getConverterId(facesConverter);
    if (converterId != null) {
        return createConverterById(facesContext, converterId);
    }
    return createConverterByClass(facesContext, facesConverter.forClass());
}
// ---- helper method(s) introduced by the refactoring ----
private FacesConverter getFacesConverterAnnotation(Class<?> wrappedClass) {
    return wrappedClass.getAnnotation(FacesConverter.class);
}

private String getConverterId(FacesConverter facesConverter) {
    String value = facesConverter.value();
    return "".equals(value) ? null : value;
}

private Converter createConverterById(FacesContext facesContext, String converterId) {
    return facesContext.getApplication().createConverter(converterId);
}

private Converter createConverterByClass(FacesContext facesContext, Class<?> converterClass) {
    return facesContext.getApplication().createConverter(converterClass);
}

