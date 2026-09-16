@Override
protected void onInitialize() {
    newRecord.setDefaultModelObject(getString("155"));
    add(newRecord.setVisible(false).setOutputMarkupId(true));
    createNewButton();
    createDeleteButton();
    createRestoreButton();
    add(newBtn, delBtn, restoreBtn.setOutputMarkupPlaceholderTag(true).setVisible(false));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void createNewButton() {
    newBtn = new AjaxButton("btn-new", form) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            // repaint the feedback panel so that it is hidden
            target.add(feedback);
            newRecord.setVisible(true);
            target.add(newRecord);
            onNewSubmit(target, form);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            // repaint the feedback panel so errors are shown
            target.add(feedback);
            AdminActionsPanel.this.onError(target, form);
        }
    };
}

private void createDeleteButton() {
    delBtn = new AjaxLink<>("btn-delete") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            // repaint the feedback panel so that it is hidden
            target.add(feedback);
            setNewRecordVisible(false);
            onDeleteSubmit(target, form);
        }
    };
    delBtn.add(newOkCancelDangerConfirm(this, getString("833")));
}

private void createRestoreButton() {
    restoreBtn = new AjaxLink<>("btn-restore") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            // repaint the feedback panel so that it is hidden
            target.add(feedback);
            setNewRecordVisible(false);
            onRestoreSubmit(target, form);
        }
    };
}

