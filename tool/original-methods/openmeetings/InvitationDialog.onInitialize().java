@Override
protected void onInitialize() {
    header(new ResourceModel("213"));
    add(form);
    addButton(send = new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("218"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            InvitationDialog.this.onError(target);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            InvitationDialog.this.onClick(target, InvitationForm.Action.SEND);
        }
    });
    addButton(generate = new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("1526"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            InvitationDialog.this.onError(target);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            InvitationDialog.this.onClick(target, InvitationForm.Action.GENERATE);
        }
    });
    addButton(OmModalCloseButton.of());
    super.onInitialize();
}