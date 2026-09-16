@Override
protected void onInitialize() {
    header(new ResourceModel("815"));
    setCloseOnEscapeKey(false);
    setBackdrop(Backdrop.FALSE);
    size(Size.Small);
    show(true);
    super.onInitialize();
    add(new Label("title"));
    add(new Label("description").setEscapeModelStrings(false));
    add(new Label("owner.timeZoneId"));
    add(new Label("start", getDateFormat().format(getModelObject().getStart())));
    add(new Label("end", getDateFormat().format(getModelObject().getEnd())));
    add(new Label("owner.firstname"));
    add(new Label("owner.lastname"));
    addButton(OmModalCloseButton.of());
}