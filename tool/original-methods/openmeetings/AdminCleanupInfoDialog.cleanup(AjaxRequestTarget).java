public void cleanup(AjaxRequestTarget target) {
    try {
        ((CleanupEntityUnit) profile.getDefaultModelObject()).cleanup();
        ((CleanupUnit) imp.getDefaultModelObject()).cleanup();
        ((CleanupUnit) backup.getDefaultModelObject()).cleanup();
        ((CleanupEntityUnit) files.getDefaultModelObject()).cleanup();
        ((CleanupEntityUnit) fin.getDefaultModelObject()).cleanup();
        update(target);
    } catch (Exception e) {
        error(getString("dashboard.widget.admin.cleanup.error"));
    }
}