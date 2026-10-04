@Override
protected void onSaveSubmit(AjaxRequestTarget target, Form<?> form) {
    try {
        LabelDao.update(panel.language.getValue(), getModelObject());
    } catch (Exception e) {
        handleException(e, "saving");
    }
    setNewRecordVisible(false);
    target.add(panel.listContainer);
}
// ---- helper method(s) introduced by the refactoring ----
private void handleException(Exception e, String action) {
    error("Unexpected error while " + action + " label: " + e.getMessage());
}

