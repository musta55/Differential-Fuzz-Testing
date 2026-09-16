@Override
protected Converter resolveInstanceForClass(FacesContext facesContext, Class<?> wrappedClass) {
    FacesConverter facesConverter = wrappedClass.getAnnotation(FacesConverter.class);
    if (facesConverter == null) {
        return null;
    }
    if (!"".equals(facesConverter.value())) {
        return facesContext.getApplication().createConverter(facesConverter.value());
    }
    return facesContext.getApplication().createConverter(facesConverter.forClass());
}