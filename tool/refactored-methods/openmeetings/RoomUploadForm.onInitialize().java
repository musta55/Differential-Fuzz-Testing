@Override
protected void onInitialize() {
    super.onInitialize();
    addContainersToForm();
}
// ---- helper method(s) introduced by the refactoring ----
private void addContainersToForm() {
    form.add(lastSelectedId.setMarkupId(PARAM_LAST_SELECTED_ID).setOutputMarkupId(true));
    form.add(lastSelectedRoom.setMarkupId(PARAM_LAST_SELECTED_ROOM).setOutputMarkupId(true));
    form.add(lastSelectedOwner.setMarkupId(PARAM_LAST_SELECTED_OWNER).setOutputMarkupId(true));
    form.add(lastSelectedGroup.setMarkupId(PARAM_LAST_SELECTED_GROUP).setOutputMarkupId(true));
}

private void updateLastSelectedWithoutId(BaseFileItem last) {
    lastSelectedRoom.add(AttributeModifier.replace(ATTR_VALUE, last.getRoomId()));
    lastSelectedOwner.add(AttributeModifier.replace(ATTR_VALUE, last.getOwnerId()));
    lastSelectedGroup.add(AttributeModifier.replace(ATTR_VALUE, last.getGroupId()));
}

private void updateLastSelectedWithId(BaseFileItem last) {
    lastSelectedId.add(AttributeModifier.replace(ATTR_VALUE, last.getId()));
}

