@Override
protected void onInitialize() {
    add(subject, message);
    recipients.setLabel(new ResourceModel("216")).setRequired(true).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        url.setModelObject(null);
        updateButtons(target);
    })).setOutputMarkupId(true);
    if (dropDownParentId != null) {
        recipients.getSettings().setDropdownParent(dropDownParentId);
    }
    add(new AjaxCheckBox("passwordProtected") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            InvitationForm.this.getModelObject().setPasswordProtected(getConvertedInput());
            passwd.setEnabled(getConvertedInput());
            target.add(passwd);
        }
    });
    RadioGroup<Valid> valid = new RadioGroup<>("valid");
    valid.add(new AjaxFormChoiceComponentUpdatingBehavior() {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            boolean dateEnabled = InvitationForm.this.getModelObject().getValid() == Valid.PERIOD;
            target.add(from.setEnabled(dateEnabled), to.setEnabled(dateEnabled), timeZoneId.setEnabled(dateEnabled));
        }
    });
    add(valid.add(new Radio<>("one", Model.of(Valid.ONE_TIME)), new Radio<>("period", Model.of(Valid.PERIOD)), new Radio<>("endless", Model.of(Valid.ENDLESS))));
    add(passwd);
    Invitation i = getModelObject();
    passwd.setLabel(new ResourceModel("110")).setOutputMarkupId(true).setEnabled(i.isPasswordProtected());
    add(from.setLabel(new ResourceModel("530")).setOutputMarkupId(true), to.setLabel(new ResourceModel("531")).setOutputMarkupId(true), timeZoneId.setOutputMarkupId(true));
    timeZoneId.add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        //no-op added to preserve selection
    }));
    add(url.setOutputMarkupId(true));
    add(lang, feedback.setOutputMarkupId(true));
    super.onInitialize();
}