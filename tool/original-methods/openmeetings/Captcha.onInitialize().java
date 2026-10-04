@Override
protected void onInitialize() {
    super.onInitialize();
    lang = LabelDao.getOmLanguage(WebSession.get().getLocale(), getDefaultLang());
    add(captcha.setOutputMarkupId(true));
    add(captchaText.setLabel(new ResourceModel("captcha.text")).add(new IValidator<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            if (!randomText.equals(validatable.getValue())) {
                validatable.error(new ValidationError(getString("bad.captcha.text")));
            }
        }
    }).setOutputMarkupId(true).add(AttributeModifier.append("placeholder", lang.getTip())));
    add(new BootstrapAjaxLink<>("refresh", Model.of(""), Buttons.Type.Outline_Info, new ResourceModel("lbl.refresh")) {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            captchaImageResource.invalidate();
            target.add(captcha);
        }

        @Override
        protected Icon newIcon(String markupId) {
            return new Icon(markupId, FontAwesome6IconType.rotate_s);
        }
    });
}