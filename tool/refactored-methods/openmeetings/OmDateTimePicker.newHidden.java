@Override
protected HiddenField<LocalDateTime> newHidden(String wicketId, IModel<LocalDateTime> model) {
    final IConverter<?> converter = new LocalDateTimeConverterImpl();
    return new HiddenField<>(wicketId, model, LocalDateTime.class) {

        private static final long serialVersionUID = 1L;

        @Override
        protected IConverter<?> createConverter(Class<?> clazz) {
            if (LocalDateTime.class.isAssignableFrom(clazz)) {
                return converter;
            }
            return null;
        }
    };
}