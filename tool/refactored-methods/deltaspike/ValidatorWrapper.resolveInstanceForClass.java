@Override
protected Validator resolveInstanceForClass(FacesContext facesContext, Class<?> wrappedClass) {
    if (!hasFacesValidatorAnnotation(wrappedClass)) {
        return null;
    }
    return facesContext.getApplication().createValidator(getValidatorId(wrappedClass));
}
// ---- helper method(s) introduced by the refactoring ----
private boolean hasFacesValidatorAnnotation(Class<?> wrappedClass) {
    return wrappedClass.getAnnotation(FacesValidator.class) != null;
}

private String getValidatorId(Class<?> wrappedClass) {
    return wrappedClass.getAnnotation(FacesValidator.class).value();
}

