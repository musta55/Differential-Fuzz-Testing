@Override
protected HiddenField<LocalDate> newHidden(String wicketId, IModel<LocalDate> model) {
    final IConverter<?> converter = createLocalDateConverter();
    HiddenField<LocalDate> date = new HiddenField<>(wicketId, model, LocalDate.class) {

        private static final long serialVersionUID = 1L;

        @Override
        protected IConverter<?> createConverter(Class<?> clazz) {
            return createConverterForClass(clazz, converter);
        }
    };
    date.add(OnChangeAjaxBehavior.onChange(this::onValueChanged));
    return date;
}
// ---- helper method(s) introduced by the refactoring ----
private IConverter<?> createLocalDateConverter() {
    return new LocalDateConverter() {

        private static final long serialVersionUID = 1L;

        @Override
        public DateTimeFormatter getDateTimeFormatter(Locale locale) {
            return DateTimeFormatter.ISO_LOCAL_DATE;
        }
    };
}

private IConverter<?> createConverterForClass(Class<?> clazz, IConverter<?> converter) {
    if (LocalDate.class.isAssignableFrom(clazz)) {
        return converter;
    }
    return null;
}

