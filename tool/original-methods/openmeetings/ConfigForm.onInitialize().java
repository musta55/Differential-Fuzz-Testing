@Override
protected void onInitialize() {
    super.onInitialize();
    add(new DateLabel("updated"));
    add(new Label("user.login"));
    add(new TextArea<String>("comment"));
    update(null);
    add(new DropDownChoice<>("type", List.of(Type.values()), new LambdaChoiceRenderer<>(Type::name, Type::name)).setLabel(new ResourceModel("45")).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, this::update)));
    add(new RequiredTextField<String>("key").setLabel(new ResourceModel("265")).add(new IValidator<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            Configuration c = cfgDao.forceGet(validatable.getValue());
            if (c != null && !c.isDeleted() && !c.getId().equals(ConfigForm.this.getModelObject().getId())) {
                validatable.error(new ValidationError(getString("error.cfg.exist")));
            }
        }
    }).add(maximumLength(255)));
    valueS.add(maximumLength(255));
    valueS.add(new IValidator<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            Configuration c = getModelFixType();
            if (Type.PATH == c.getType()) {
                try {
                    Path.of(validatable.getValue());
                } catch (InvalidPathException e) {
                    validatable.error(new ValidationError(e.getMessage()));
                }
            }
        }
    });
    stringBox.add(valueS.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
    numberBox.add(valueN.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
    booleanBox.add(valueB.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
    hotkeyBox.add(valueH.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
    add(stringBox, numberBox, booleanBox, hotkeyBox);
    setNewRecordVisible(true);
}