@Override
protected void onInitialize() {
    header(new ResourceModel("1003"));
    setCloseOnEscapeKey(false);
    setBackdrop(Backdrop.STATIC);
    AjaxButton submitButton = createSubmitButton();
    form.setDefaultButton(submitButton);
    add(feedback.setOutputMarkupId(true), form.add(number, submitButton));
    addButton(createHangupButton());
    addButton(createCallButton());
    addButton(OmModalCloseButton.of("85"));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private AjaxButton createSubmitButton() {
    return new //FAKE button so "submit-on-enter" works as expected
    AjaxButton(//FAKE button so "submit-on-enter" works as expected
    "submit") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            SipDialerDialog.this.onSubmit(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            SipDialerDialog.this.onError(target);
        }
    };
}

private BootstrapAjaxButton createHangupButton() {
    return new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("label.hangup"), form, Buttons.Type.Outline_Danger) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            sipDao.hangup(room.getRoom());
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            SipDialerDialog.this.onError(target);
        }
    };
}

private BootstrapAjaxButton createCallButton() {
    return new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("1448"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            SipDialerDialog.this.onSubmit(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            SipDialerDialog.this.onError(target);
        }
    };
    // call
}

