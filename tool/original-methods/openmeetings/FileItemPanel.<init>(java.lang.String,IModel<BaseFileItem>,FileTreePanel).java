public FileItemPanel(String id, final IModel<BaseFileItem> model, final FileTreePanel fileTreePanel) {
    super(id, model, fileTreePanel);
    BaseFileItem f = model.getObject();
    long errorCount = fileLogDao.countErrors(f);
    boolean visible = errorCount != 0;
    if (BaseFileItem.Type.RECORDING == f.getType()) {
        Recording r = (Recording) f;
        visible |= (Status.RECORDING != r.getStatus() && Status.CONVERTING != r.getStatus() && !f.exists());
    } else {
        visible |= !f.exists();
    }
    errors.add(new AjaxEventBehavior(EVT_CLICK) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            fileTreePanel.errorsDialog.setDefaultModel(model);
            fileTreePanel.errorsDialog.show(target);
        }
    }).setVisible(visible);
    add(errors);
}