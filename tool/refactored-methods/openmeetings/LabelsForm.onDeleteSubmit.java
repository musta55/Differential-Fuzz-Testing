@Override
protected void onDeleteSubmit(AjaxRequestTarget target, Form<?> form) {
    try {
        LabelDao.delete(panel.language.getValue(), getModelObject());
    } catch (Exception e) {
        handleException(e, "deleting");
    }
    target.add(panel.listContainer);
}
// ---- helper method(s) introduced by the refactoring ----
private void handleException(Exception e, String action) {
    error("Unexpected error while " + action + " label: " + e.getMessage());
}

