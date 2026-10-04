@Override
protected void onInitialize() {
    newRecord.setDefaultModelObject(getString("155"));
    add(newRecord.setVisible(false).setOutputMarkupId(true));
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
    // add a cancel button that can be used to submit the form via ajax
    final Form<?> cForm = new Form<>("form");
    cForm.setMultiPart(form.isMultiPart());
    add(cForm);
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
    add(newBtn, delBtn, restoreBtn.setOutputMarkupPlaceholderTag(true).setVisible(false));
    super.onInitialize();
}