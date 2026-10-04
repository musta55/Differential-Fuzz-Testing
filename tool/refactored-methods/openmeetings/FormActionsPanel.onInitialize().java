@Override
protected void onInitialize() {
    add(feedback.setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true));
    add(createSaveButton());
    add(createRefreshButton());
    purgeBtn = createPurgeButton();
    purgeBtn.add(newOkCancelDangerConfirm(this, getString("admin.purge.desc")));
    add(purgeBtn.setOutputMarkupPlaceholderTag(true).setVisible(false));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private AjaxButton createSaveButton() {
    return new AjaxButton("btn-save", form) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            target.add(feedback);
            onSaveSubmit(target, form);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
            FormActionsPanel.this.onError(target, form);
        }
    };
}

private AjaxLink<Void> createRefreshButton() {
    return new AjaxLink<Void>("btn-refresh") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            target.add(feedback);
            setNewRecordVisible(false);
            onRefreshSubmit(target, form);
        }
    };
}

private AjaxLink<Void> createPurgeButton() {
    return new AjaxLink<>("btn-purge") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            target.add(feedback);
            setNewRecordVisible(false);
            onPurgeSubmit(target, form);
        }
    };
}

