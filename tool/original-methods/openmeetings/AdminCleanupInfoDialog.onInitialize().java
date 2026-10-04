@Override
protected void onInitialize() {
    super.onInitialize();
    header(new ResourceModel("dashboard.widget.admin.cleanup.title"));
    setCloseOnEscapeKey(true);
    addButton(OmModalCloseButton.of("54"));
    uploadSize = new Label("upload-size", "");
    profile = new CleanupEntityUnitPanel("profile", "dashboard.widget.admin.cleanup.profiles", new CleanupEntityUnit());
    imp = new CleanupUnitPanel("import", "dashboard.widget.admin.cleanup.import", new CleanupUnit());
    backup = new CleanupUnitPanel("backup", "dashboard.widget.admin.cleanup.backup", new CleanupUnit());
    files = new CleanupEntityUnitPanel("files", "dashboard.widget.admin.cleanup.files", new CleanupEntityUnit());
    streamsSize = new Label("streams-size", "");
    fin = new CleanupEntityUnitPanel("final", "dashboard.widget.admin.cleanup.final", new CleanupEntityUnit());
    add(feedback.setOutputMarkupId(true));
    add(container.add(uploadSize, profile, imp, backup, files, streamsSize, fin).setOutputMarkupId(true));
    add(new Form<Void>("form") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onInitialize() {
            super.onInitialize();
            SpinnerAjaxButton cleanup = new SpinnerAjaxButton("cleanup", new ResourceModel("dashboard.widget.admin.cleanup.cleanup"), this, Buttons.Type.Outline_Danger) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    cleanup(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                    target.add(feedback);
                }
            };
            add(cleanup.add(newOkCancelDangerConfirm(this, getString("dashboard.widget.admin.cleanup.warn"))));
        }
    });
}