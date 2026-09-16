private void update(AjaxRequestTarget target) {
    updateVisibility(getModelObject());
    updateComponents(target);
}
// ---- helper method(s) introduced by the refactoring ----
private void updateVisibility(Configuration c) {
    stringBox.setVisible(Type.PATH == c.getType() || Type.STRING == c.getType());
    numberBox.setVisible(Type.NUMBER == c.getType());
    booleanBox.setVisible(Type.BOOL == c.getType());
    hotkeyBox.setVisible(Type.HOTKEY == c.getType());
}

private void updateComponents(AjaxRequestTarget target) {
    if (target != null) {
        target.add(stringBox, numberBox, booleanBox, hotkeyBox);
        if (Type.HOTKEY == getModelObject().getType()) {
            target.appendJavaScript("addOmAdminConfigHandlers()");
        }
    }
}

private void addDateLabel() {
    add(new DateLabel("updated"));
}

private void addUserLoginLabel() {
    add(new Label("user.login"));
}

private void addCommentTextArea() {
    add(new TextArea<String>("comment"));
}

private void addTypeDropDownChoice() {
    add(new DropDownChoice<>("type", List.of(Type.values()), new LambdaChoiceRenderer<>(Type::name, Type::name)).setLabel(new ResourceModel("45")).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, this::update)));
}

private void addKeyRequiredTextField() {
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
}

private void addValueSTextArea() {
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
}

private void addValueNTextField() {
    numberBox.add(valueN.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
}

private void addValueBCheckBox() {
    booleanBox.add(valueB.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
}

private void addValueHTextField() {
    hotkeyBox.add(valueH.setLabel(new ResourceModel("271"))).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
}

private void addBoxesToForm() {
    add(stringBox, numberBox, booleanBox, hotkeyBox);
}

private void setNewRecordVisibility() {
    setNewRecordVisible(true);
}

private void initializeComponents() {
    addDateLabel();
    addUserLoginLabel();
    addCommentTextArea();
    update(null);
    addTypeDropDownChoice();
    addKeyRequiredTextField();
    addValueSTextArea();
    addValueNTextField();
    addValueBCheckBox();
    addValueHTextField();
    addBoxesToForm();
    setNewRecordVisibility();
}

