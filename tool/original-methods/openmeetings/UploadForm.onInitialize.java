@Override
protected void onInitialize() {
    final MainPanel mainPanel = findParent(MainPanel.class);
    add(form.add(AttributeModifier.append("data-max-size", getMaxUploadSize())).add(AttributeModifier.append("data-max-size-lbl", Bytes.bytes(getMaxUploadSize()).toString(WebSession.get().getLocale()))).add(AttributeModifier.append("data-upload-lbl", getString(buttonLabelKey()))).add(AttributeModifier.append("action", action)).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
    form.add(new WebMarkupContainer("sid").add(AttributeModifier.append("value", mainPanel.getClient().getSid())).setOutputMarkupId(true));
    WebMarkupContainer file = new WebMarkupContainer("file");
    if (allowMultiple()) {
        file.add(AttributeModifier.append("multiple", "multiple"));
    }
    form.add(file);
    form.add(new WebMarkupContainer("desc-block").setVisible(showDescBlock()));
    // set max upload size in form as info text
    Long maxBytes = getMaxUploadSize();
    double megaBytes = maxBytes.doubleValue() / 1024 / 1024;
    DecimalFormat formatter = new DecimalFormat("#,###.00");
    form.add(new Label("MaxUploadSize", formatter.format(megaBytes)));
    form.add(new Label("btn-label", new ResourceModel(buttonLabelKey())));
    add(new WebMarkupContainer("progress-title").add(AttributeModifier.append("data-processing-lbl", getString(processingLabelKey()))));
    add(BootstrapFileUploadBehavior.getInstance());
    super.onInitialize();
}