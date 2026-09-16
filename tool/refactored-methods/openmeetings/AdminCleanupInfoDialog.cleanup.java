public void cleanup(AjaxRequestTarget target) {
    try {
        cleanupProfile();
        cleanupImport();
        cleanupBackup();
        cleanupFiles();
        cleanupFinal();
        update(target);
    } catch (Exception e) {
        error(getString("dashboard.widget.admin.cleanup.error"));
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void addComponents() {
    uploadSize = new Label("upload-size", "");
    profile = new CleanupEntityUnitPanel("profile", "dashboard.widget.admin.cleanup.profiles", new CleanupEntityUnit());
    imp = new CleanupUnitPanel("import", "dashboard.widget.admin.cleanup.import", new CleanupUnit());
    backup = new CleanupUnitPanel("backup", "dashboard.widget.admin.cleanup.backup", new CleanupUnit());
    files = new CleanupEntityUnitPanel("files", "dashboard.widget.admin.cleanup.files", new CleanupEntityUnit());
    streamsSize = new Label("streams-size", "");
    fin = new CleanupEntityUnitPanel("final", "dashboard.widget.admin.cleanup.final", new CleanupEntityUnit());
    add(feedback.setOutputMarkupId(true));
    add(container.add(uploadSize, profile, imp, backup, files, streamsSize, fin).setOutputMarkupId(true));
}

private void addForm() {
    add(new Form<Void>("form") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onInitialize() {
            super.onInitialize();
            SpinnerAjaxButton cleanupButton = new SpinnerAjaxButton("cleanup", new ResourceModel("dashboard.widget.admin.cleanup.cleanup"), this, Buttons.Type.Outline_Danger) {

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
            add(cleanupButton.add(newOkCancelDangerConfirm(this, getString("dashboard.widget.admin.cleanup.warn"))));
        }
    });
}

private void cleanupProfile() throws Exception {
    ((CleanupEntityUnit) profile.getDefaultModelObject()).cleanup();
}

private void cleanupImport() throws Exception {
    ((CleanupUnit) imp.getDefaultModelObject()).cleanup();
}

private void cleanupBackup() throws Exception {
    ((CleanupUnit) backup.getDefaultModelObject()).cleanup();
}

private void cleanupFiles() throws Exception {
    ((CleanupEntityUnit) files.getDefaultModelObject()).cleanup();
}

private void cleanupFinal() throws Exception {
    ((CleanupEntityUnit) fin.getDefaultModelObject()).cleanup();
}

