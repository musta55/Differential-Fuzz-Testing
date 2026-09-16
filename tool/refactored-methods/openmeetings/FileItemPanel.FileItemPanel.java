public FileItemPanel(String id, final IModel<BaseFileItem> model, final FileTreePanel fileTreePanel) {
    super(id, model, fileTreePanel);
    BaseFileItem f = model.getObject();
    boolean visible = shouldShowErrors(f);
    errors.add(createErrorClickBehavior(fileTreePanel, model)).setVisible(visible);
    add(errors);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldShowErrors(BaseFileItem f) {
    long errorCount = fileLogDao.countErrors(f);
    boolean visible = errorCount != 0;
    if (BaseFileItem.Type.RECORDING == f.getType()) {
        Recording r = (Recording) f;
        visible |= (Status.RECORDING != r.getStatus() && Status.CONVERTING != r.getStatus() && !f.exists());
    } else {
        visible |= !f.exists();
    }
    return visible;
}

private AjaxEventBehavior createErrorClickBehavior(final FileTreePanel fileTreePanel, final IModel<BaseFileItem> model) {
    return new AjaxEventBehavior(EVT_CLICK) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            fileTreePanel.errorsDialog.setDefaultModel(model);
            fileTreePanel.errorsDialog.show(target);
        }
    };
}

