@Override
protected void onInitialize() {
    header(new ResourceModel("204"));
    setCloseOnEscapeKey(false);
    show(autoOpen);
    withLabel(new ResourceModel(labelId));
    getLabel().setOutputMarkupId(true);
    withErrorIcon();
    super.onInitialize();
    if (autoOpen) {
        startTimer(null);
    }
}