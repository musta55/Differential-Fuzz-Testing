@Override
protected Validator resolveInstanceForClass(FacesContext facesContext, Class<?> wrappedClass) {
    FacesValidator facesValidator = wrappedClass.getAnnotation(FacesValidator.class);
    if (facesValidator == null) {
        return null;
    }
    return facesContext.getApplication().createValidator(facesValidator.value());
}