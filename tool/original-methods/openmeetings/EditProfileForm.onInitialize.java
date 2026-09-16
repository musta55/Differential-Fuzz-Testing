@Override
protected void onInitialize() {
    super.onInitialize();
    add(passwd.setLabel(new ResourceModel("current.password")).setRequired(true).setVisible(checkPassword));
    add(actions = new FormActionsPanel<>("buttons", this) {

        private static final long serialVersionUID = 1L;

        private void refreshUser() {
            User u = getModelObject();
            if (u.getId() != null) {
                u = userDao.get(u.getId());
            } else {
                u = new User();
            }
            setModelObject(u);
        }

        @Override
        protected void onSaveSubmit(AjaxRequestTarget target, Form<?> form) {
            try {
                userDao.update(getModelObject(), null, getUserId());
            } catch (Exception e) {
                error(e.getMessage());
            }
            refreshUser();
            target.add(EditProfileForm.this);
        }

        @Override
        protected void onRefreshSubmit(AjaxRequestTarget target, Form<?> form) {
            refreshUser();
            target.add(EditProfileForm.this);
        }

        @Override
        protected void onPurgeSubmit(AjaxRequestTarget target, Form<?> form) {
            userDao.purge(getModelObject(), getUserId());
            WebSession.get().invalidateNow();
            setResponsePage(Application.get().getSignInPageClass());
        }
    });
    add(new BootstrapAjaxLink<>("changePwd", Model.of(""), Buttons.Type.Outline_Danger, new ResourceModel("327")) {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            ChangePasswordDialog dlg = (ChangePasswordDialog) findParent(EditProfilePanel.class).get("changePasswdDlg");
            dlg.show(target);
        }
    }.setVisible(checkPassword));
    toggleOtp = new BootstrapAjaxLink<>("toggleOtp", null, Buttons.Type.Outline_Danger, Model.of("")) {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            User u = EditProfileForm.this.getModelObject();
            if (u.getOtpSecret() == null) {
                ToggleOtpDialog dlg = (ToggleOtpDialog) findParent(EditProfilePanel.class).get("toggleOtpDlg");
                dlg.setModel(EditProfileForm.this.getModel());
                dlg.show(target);
            } else {
                u.setOtpSecret(null);
                u.setOtpRecoveryCodes(null);
                updateOtpButton(false, target);
            }
        }
    };
    add(toggleOtp.setOutputMarkupId(true).setVisible(isOtpEnabled() && checkPassword));
    updateOtpButton(getModelObject().getOtpSecret() != null, null);
    add(userForm);
    add(new UploadableProfileImagePanel("img", getUserId()));
    add(new CommunityUserForm("comunity", getModel()));
    // attach an ajax validation behavior to all form component's keydown
    // event and throttle it down to once per second
    add(new AjaxFormValidatingBehavior("keydown", Duration.ofSeconds(1)));
    add(new BookmarkablePageLink<>("link", PrivacyPage.class));
}