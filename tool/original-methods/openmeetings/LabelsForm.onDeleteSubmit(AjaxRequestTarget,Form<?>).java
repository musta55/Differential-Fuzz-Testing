@Override
protected void onDeleteSubmit(AjaxRequestTarget target, Form<?> form) {
    try {
        LabelDao.delete(panel.language.getValue(), getModelObject());
    } catch (Exception e) {
        error("Unexpected error while deleting label:" + e.getMessage());
    }
    target.add(panel.listContainer);
}