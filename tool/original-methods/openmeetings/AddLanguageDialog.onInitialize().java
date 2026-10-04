@Override
protected void onInitialize() {
    header(new ResourceModel("362"));
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("366"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            try {
                LabelDao.add(Locale.forLanguageTag(iso.getModelObject()));
                langPanel.getLangForm().updateLanguages(target);
                AddLanguageDialog.this.close(target);
            } catch (Exception e) {
                error("Failed to add, " + e.getMessage());
                target.add(feedback);
            }
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }
    });
    add(form.add(feedback.setOutputMarkupId(true), iso.setOutputMarkupId(true)));
    iso.add(new IValidator<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> s) {
            try {
                new Locale.Builder().setLanguageTag(s.getValue()).build();
            } catch (IllformedLocaleException e) {
                s.error(new ValidationError("Invalid code, please specify valid ISO code"));
                return;
            }
            Locale l = Locale.forLanguageTag(s.getValue());
            for (Map.Entry<Long, Locale> e : LabelDao.getLanguages()) {
                if (e.getValue().equals(l)) {
                    s.error(new ValidationError("This code already added"));
                    break;
                }
            }
        }
    });
    super.onInitialize();
}