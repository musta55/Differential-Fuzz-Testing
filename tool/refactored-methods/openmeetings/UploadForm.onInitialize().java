@Override
protected void onInitialize() {
    addFormComponents();
    addFileComponent();
    addDescriptionBlock();
    addMaxUploadSizeInfo();
    addButtonLabel();
    addProgressTitle();
    addBootstrapFileUploadBehavior();
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void addFormComponents() {
    final MainPanel mainPanel = findParent(MainPanel.class);
    form.add(AttributeModifier.append("data-max-size", getMaxUploadSize())).add(AttributeModifier.append("data-max-size-lbl", Bytes.bytes(getMaxUploadSize()).toString(WebSession.get().getLocale()))).add(AttributeModifier.append("data-upload-lbl", getString(buttonLabelKey()))).add(AttributeModifier.append("action", action)).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
    form.add(new WebMarkupContainer("sid").add(AttributeModifier.append("value", mainPanel.getClient().getSid())).setOutputMarkupId(true));
    add(form);
}

private void addFileComponent() {
    WebMarkupContainer file = new WebMarkupContainer("file");
    if (allowMultiple()) {
        file.add(AttributeModifier.append("multiple", "multiple"));
    }
    form.add(file);
}

private void addDescriptionBlock() {
    form.add(new WebMarkupContainer("desc-block").setVisible(showDescBlock()));
}

private void addMaxUploadSizeInfo() {
    Long maxBytes = getMaxUploadSize();
    double megaBytes = maxBytes.doubleValue() / 1024 / 1024;
    DecimalFormat formatter = new DecimalFormat("#,###.00");
    form.add(new Label("MaxUploadSize", formatter.format(megaBytes)));
}

private void addButtonLabel() {
    form.add(new Label("btn-label", new ResourceModel(buttonLabelKey())));
}

private void addProgressTitle() {
    add(new WebMarkupContainer("progress-title").add(AttributeModifier.append("data-processing-lbl", getString(processingLabelKey()))));
}

private void addBootstrapFileUploadBehavior() {
    add(BootstrapFileUploadBehavior.getInstance());
}

