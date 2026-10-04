@Override
protected void onInitialize() {
    header(new ResourceModel("230"));
    setCloseOnEscapeKey(false);
    setBackdrop(Backdrop.STATIC);
    password.add(new PasswordValidator());
    add(form.add(password, feedback.setOutputMarkupId(true)));
    AjaxButton ab = new SubmitButton("submit", form);
    form.add(ab);
    form.setDefaultButton(ab);
    password.setLabel(new ResourceModel("110"));
    addButton(new DialogSubmitButton(BUTTON_MARKUP_ID, new ResourceModel("537"), form, Buttons.Type.Outline_Primary));
    super.onInitialize();
    Invitation i = WebSession.get().getInvitation();
    show(i != null && i.isPasswordProtected());
}