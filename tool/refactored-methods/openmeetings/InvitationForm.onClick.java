public void onClick(AjaxRequestTarget target, Action action) {
    String userbaseUrl = WebSession.get().getExtendedProperties().getBaseUrl();
    if (Action.GENERATE == action) {
        handleGenerateAction(target, userbaseUrl);
    } else {
        handleSendAction(target, userbaseUrl);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void addFormComponents() {
    add(subject, message);
    recipients.setLabel(new ResourceModel("216")).setRequired(true).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        url.setModelObject(null);
        updateButtons(target);
    })).setOutputMarkupId(true);
    if (dropDownParentId != null) {
        recipients.getSettings().setDropdownParent(dropDownParentId);
    }
}

private void addPasswordProtectionCheckbox() {
    AjaxCheckBox passwordProtected = new AjaxCheckBox("passwordProtected") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            InvitationForm.this.getModelObject().setPasswordProtected(getConvertedInput());
            passwd.setEnabled(getConvertedInput());
            target.add(passwd);
        }
    };
    add(passwordProtected);
}

private void addValidityRadioGroup() {
    RadioGroup<Valid> valid = new RadioGroup<>("valid");
    valid.add(new AjaxFormChoiceComponentUpdatingBehavior() {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            boolean dateEnabled = InvitationForm.this.getModelObject().getValid() == Valid.PERIOD;
            target.add(from.setEnabled(dateEnabled), to.setEnabled(dateEnabled), timeZoneId.setEnabled(dateEnabled));
        }
    });
    valid.add(new Radio<>("one", Model.of(Valid.ONE_TIME)));
    valid.add(new Radio<>("period", Model.of(Valid.PERIOD)));
    valid.add(new Radio<>("endless", Model.of(Valid.ENDLESS)));
    add(valid);
}

private void addTimeComponents() {
    Invitation i = getModelObject();
    passwd.setLabel(new ResourceModel("110")).setOutputMarkupId(true).setEnabled(i.isPasswordProtected());
    add(passwd);
    add(from.setLabel(new ResourceModel("530")).setOutputMarkupId(true));
    add(to.setLabel(new ResourceModel("531")).setOutputMarkupId(true));
    add(timeZoneId.setOutputMarkupId(true));
    timeZoneId.add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        //no-op added to preserve selection
    }));
}

private void addUrlField() {
    add(url.setOutputMarkupId(true));
}

private void addLanguageDropDown() {
    add(lang);
}

private void addFeedbackPanel() {
    add(feedback.setOutputMarkupId(true));
}

private void handleGenerateAction(AjaxRequestTarget target, String userbaseUrl) {
    Invitation i = create(recipients.getModelObject().iterator().next());
    setModelObject(i);
    url.setModelObject(getInvitationLink(i, userbaseUrl));
    target.add(url);
}

private void handleSendAction(AjaxRequestTarget target, String userbaseUrl) {
    if (Strings.isEmpty(url.getModelObject())) {
        sendInvitationsToRecipients(userbaseUrl);
    } else {
        sendInvitationByUrl(userbaseUrl);
    }
    dialog.close(target);
}

private void sendInvitationsToRecipients(String userbaseUrl) {
    for (User u : recipients.getModelObject()) {
        Invitation i = create(u);
        try {
            inviteManager.sendInvitationLink(i, MessageType.CREATE, subject.getModelObject(), message.getModelObject(), false, userbaseUrl);
        } catch (Exception e) {
            log.error("error while sending invitation by User ", e);
        }
    }
}

private void sendInvitationByUrl(String userbaseUrl) {
    Invitation i = getModelObject();
    try {
        inviteManager.sendInvitationLink(i, MessageType.CREATE, subject.getModelObject(), message.getModelObject(), false, userbaseUrl);
    } catch (Exception e) {
        log.error("error while sending invitation by URL ", e);
    }
}

