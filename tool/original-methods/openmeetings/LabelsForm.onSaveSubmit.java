@Override
protected void onSaveSubmit(AjaxRequestTarget target, Form<?> form) {
    try {
        LabelDao.update(panel.language.getValue(), getModelObject());
    } catch (Exception e) {
        error("Unexpected error while saving label:" + e.getMessage());
    }
    setNewRecordVisible(false);
    target.add(panel.listContainer);
}