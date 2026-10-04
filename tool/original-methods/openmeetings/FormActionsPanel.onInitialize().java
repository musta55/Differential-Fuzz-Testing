@Override
protected void onInitialize() {
    add(feedback.setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true));
    // add a save button that can be used to submit the form via ajax
    add(saveBtn = new AjaxButton("btn-save", form) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            // repaint the feedback panel so that it is hidden
            target.add(feedback);
            onSaveSubmit(target, form);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            // repaint the feedback panel so errors are shown
            target.add(feedback);
            FormActionsPanel.this.onError(target, form);
        }
    });
    // add a refresh button that can be used to submit the form via ajax
    add(new AjaxLink<Void>("btn-refresh") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            // repaint the feedback panel so that it is hidden
            target.add(feedback);
            setNewRecordVisible(false);
            onRefreshSubmit(target, form);
        }
    });
    purgeBtn = new AjaxLink<>("btn-purge") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            // repaint the feedback panel so that it is hidden
            target.add(feedback);
            setNewRecordVisible(false);
            onPurgeSubmit(target, form);
        }
    };
    purgeBtn.add(newOkCancelDangerConfirm(this, getString("admin.purge.desc")));
    add(purgeBtn.setOutputMarkupPlaceholderTag(true).setVisible(false));
    super.onInitialize();
}