@Override
protected void onInitialize() {
    header(new ResourceModel("1003"));
    setCloseOnEscapeKey(false);
    setBackdrop(Backdrop.STATIC);
    AjaxButton ab = new //FAKE button so "submit-on-enter" works as expected
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
    form.setDefaultButton(ab);
    add(feedback.setOutputMarkupId(true), form.add(number, ab));
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("label.hangup"), form, Buttons.Type.Outline_Danger) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            sipDao.hangup(room.getRoom());
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            SipDialerDialog.this.onError(target);
        }
    });
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("1448"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            SipDialerDialog.this.onSubmit(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            SipDialerDialog.this.onError(target);
        }
    });
    // call
    addButton(OmModalCloseButton.of("85"));
    super.onInitialize();
}