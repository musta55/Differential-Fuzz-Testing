@Override
protected void onInitialize() {
    setupForm();
    setupButton();
    addComponentsToForm();
    handleVisibility();
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupForm() {
    add(form = new Form<>("form", new CompoundPropertyModel<>(room.getClient().getUser())));
}

private void setupButton() {
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("54"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            final User u = form.getModelObject();
            final Client c = room.getClient();
            c.getUser().setFirstname(u.getFirstname()).setLastname(u.getLastname());
            room.broadcast(cm.update(c));
            NicknameDialog.this.close(target);
        }
    });
}

private void addComponentsToForm() {
    form.add(feedback.setOutputMarkupId(true));
    form.add(new RequiredTextField<String>("firstname").setLabel(new ResourceModel("135")).add(minimumLength(getMinFnameLength())));
    form.add(new RequiredTextField<String>("lastname").setLabel(new ResourceModel("136")).add(minimumLength(getMinLnameLength())));
    form.add(new RequiredTextField<String>("address.email").setLabel(new ResourceModel("119")).add(RfcCompliantEmailAddressValidator.getInstance()));
}

private void handleVisibility() {
    User u = form.getModelObject();
    boolean visible = isVisible(u);
    if (visible) {
        u.setFirstname(getString("433"));
        u.setLastname(String.format("%s %s", u.getFirstname(), TIME_DF.format(new Date())));
    }
    show(visible);
}

