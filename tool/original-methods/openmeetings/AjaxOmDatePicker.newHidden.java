@Override
protected HiddenField<LocalDate> newHidden(String wicketId, IModel<LocalDate> model) {
    final IConverter<?> converter = new LocalDateConverter() {

        private static final long serialVersionUID = 1L;

        @Override
        public DateTimeFormatter getDateTimeFormatter(Locale locale) {
            return DateTimeFormatter.ISO_LOCAL_DATE;
        }
    };
    HiddenField<LocalDate> date = new HiddenField<>(wicketId, model, LocalDate.class) {

        private static final long serialVersionUID = 1L;

        @Override
        protected IConverter<?> createConverter(Class<?> clazz) {
            if (LocalDate.class.isAssignableFrom(clazz)) {
                return converter;
            }
            return null;
        }
    };
    date.add(OnChangeAjaxBehavior.onChange(this::onValueChanged));
    return date;
}