@Override
protected void onInitialize() {
    super.onInitialize();
    form.add(lastSelectedId.setMarkupId(PARAM_LAST_SELECTED_ID).setOutputMarkupId(true));
    form.add(lastSelectedRoom.setMarkupId(PARAM_LAST_SELECTED_ROOM).setOutputMarkupId(true));
    form.add(lastSelectedOwner.setMarkupId(PARAM_LAST_SELECTED_OWNER).setOutputMarkupId(true));
    form.add(lastSelectedGroup.setMarkupId(PARAM_LAST_SELECTED_GROUP).setOutputMarkupId(true));
}