@Override
protected void onInitialize() {
    header(new ResourceModel("327"));
    addButton(new SpinnerAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("327"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            try {
                userDao.update(userDao.get(getUserId()), pass.getModelObject(), getUserId());
                ChangePasswordDialog.this.close(target);
            } catch (Exception e) {
                error(e.getMessage());
                target.add(feedback);
            }
        }
    });
    //send
    addButton(OmModalCloseButton.of());
    StrongPasswordValidator passValidator = new StrongPasswordValidator(userDao.get(getUserId()));
    add(form.add(current.setLabel(new ResourceModel("current.password")).setRequired(true).setOutputMarkupId(true), pass.setLabel(new ResourceModel("328")).add(passValidator).setOutputMarkupId(true), pass2.setLabel(new ResourceModel("116")).setOutputMarkupId(true), feedback.setOutputMarkupId(true)));
    super.onInitialize();
}